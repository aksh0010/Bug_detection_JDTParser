import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Stack;
import org.eclipse.jdt.core.dom.*;
import org.apache.commons.io.FileUtils;

public class Lab1Driver {

	private static boolean flag_has_equals=false;
	private static boolean flag_has_hashCode=false;
	private static Stack<String> stack_all_methods = new Stack<>();
	private static Stack<String> stack_all_variables = new Stack<>();
	
	public void run() throws IOException {
		
		//using apache commons to fetch content of the file directly to string
		String content = FileUtils.readFileToString(new File("C:\\Users\\akshr\\eclipse-workspace\\Comp4110_Assignment3\\src\\Person.java"));
		
		//parser object with Java version 21
		ASTParser parser = ASTParser.newParser(AST.JLS21);
		
		//we need to give the content to the parser
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {
			/*
			 * Visiter for Method declaration 
			 * */
			@Override
			public boolean visit(MethodDeclaration node) {
				// TODO Auto-generated method stub
				
				// meaning the name is not main and is not consturcter then only we add it to our stack
				if( (!node.getName().getIdentifier().equals("main")) && !node.isConstructor()){
					
					stack_all_methods.add(node.getName().getIdentifier());
					
				}
				
				
				if(node.getName().getIdentifier().equals("equals")) {
					flag_has_equals=true;
				}
				if(node.getName().getIdentifier().equals("hashCode")) {
					flag_has_hashCode=true;
				}
			
//				System.out.println("MecodDeclaration: "+node.getName().getFullyQualifiedName());
				
				return true;
			}
			/*
			 * Visiter for Method calls / invocations
			 * */
			@Override
			public boolean visit(MethodInvocation node) {
				
				if(stack_all_methods.contains(node.getName().getIdentifier())) {
					stack_all_methods.remove(node.getName().getIdentifier());	
				}
				return true;
			}
			
			@Override
			public boolean visit(VariableDeclarationFragment node) {
			    // Add all variable names to the stack
			    stack_all_variables.add(node.getName().getIdentifier());
//			    System.out.println("\n\t"+node.getName().getIdentifier());
 
			    return true;
			}

	

			/*
			 * Visiter for switchStatement
			 * */
			@Override
			public boolean visit(SwitchStatement node) {
			   String line = "On line: " + cu.getLineNumber(node.getStartPosition());
			   String warning = "\n\tSwitch statement found where one case falls through to the next case.";

			   //storing results in list as object of statement type
			    List<Statement> statements = node.statements();
			    for (int i = 0; i < statements.size() - 1; i++) {
			        
			    	Statement current = statements.get(i);// Getting current statement and the one next to it
			        Statement next = statements.get(i + 1);
			        
			        // If two consecutive statements are of "Case", then meaning they ignore the break
			        if (current.getNodeType() == node.SWITCH_CASE && next.getNodeType() == node.SWITCH_CASE) {
			            // Two consecutive SwitchCase elements found
			        	System.err.println(line+warning);
			        	
			            return true; // Found the warning, no need to continue checking
			        }
			    }
			    return true;
			}
			/*
			 * Visiter for constant values in conditions like if
			 * */
			@Override
			public boolean visit(IfStatement node) {
				String line = "On line: " + cu.getLineNumber(node.getStartPosition());
	              
				String warning="\n\tWarning: Condition has no effect due to the variable type: The condition always produces constant true or false. The condition can be removed.";
                
				/*Fetching the node's Expression
				 * on compile time.
				 * 
				 * If we know the boolean values on compile time meaning the values
				 * will never change
				 * */
				
				
			    Expression condition = node.getExpression();
			    if (condition instanceof BooleanLiteral) {
			    	// if the condition is instance of Boolean the then fetch it and cast it to boolean
			    		BooleanLiteral value = (BooleanLiteral) condition;

			    		// simply printing the warning on err
			    	
			        	String expression = "|Found condition:("+value+")";
                    	System.err.println(line + expression + warning);
			        
                    	
                    // Now if the experssion is condition which is always true like 1==1 or something
                    //and not a boolean
			    } else  if (condition instanceof InfixExpression) {
			    	// Checking if it in infixExpression meaning from eclipse.org{Expression InfixOperator Expression}
			        InfixExpression infixExpression = (InfixExpression) condition;
			        if (infixExpression.getOperator() == InfixExpression.Operator.EQUALS) {
			           
			        	// fetching left and right operand of each side of infixexpression
			        	Expression leftOperand = infixExpression.getLeftOperand();
			            Expression rightOperand = infixExpression.getRightOperand();
			            
			            // Now i have created a function to check if the expression is 
			            // either string, char,int,null on compile time, it will be marked with
			            /// having constant results throughout
			            if (isConstantExpression(leftOperand) && isConstantExpression(rightOperand)) {
			                String expression = "| Found constant expression: " + infixExpression.toString();
			                System.err.println(line + expression + warning);
			            }
			        }
			    } 
			    // Now if the experssion is SimpleName and not a boolean    
			   else if (condition instanceof SimpleName) {
			    	//Casting the exp to simplename
			        SimpleName simpleName = (SimpleName) condition;
			        
			        IBinding binding = simpleName.resolveBinding();
			        if (binding != null && binding.getKind() == IBinding.VARIABLE) {
			            IVariableBinding variableBinding = (IVariableBinding) binding;
			            if (variableBinding.getType().getName().equals("boolean")) {
			                Object constantValue = variableBinding.getConstantValue();
			                if (constantValue != null && constantValue instanceof Boolean) {
			                		Boolean value = (Boolean) constantValue;
			                		String expression = "|Found condition:("+value+")";
			                    	System.err.println(line + expression + warning);
						      
			                    
			                }}}}
			    return true;
			}});
		
		//we need to parse the content
		
	}
	private boolean isConstantExpression(Expression expression) {
	    return expression instanceof NumberLiteral ||
	           expression instanceof BooleanLiteral ||
	           expression instanceof CharacterLiteral ||
	           expression instanceof StringLiteral ||
	           expression instanceof NullLiteral;
	}
	public static void method_usage() {
		if(stack_all_methods.size()!=0) {
			System.err.println("Warning:Some unused methods Found");
			System.err.println("\t"+stack_all_methods);
		}}
	public static void variable_usage() {
			if(stack_all_variables.size()!=0) {
				System.err.println("Warning:Some unused Variables Found");
				System.err.println("\t"+stack_all_variables);
			}
		
	}
	public static void main(String[] args) {
		
		Lab1Driver driver = new Lab1Driver();
		try {
			driver.run();
			
			method_usage();
			variable_usage();
			// Checking for Equals method with no HashCode method and throwing Warning on err
			if(flag_has_equals) {
				
				if(!flag_has_hashCode) {
					
					System.err.println("Warning :Found equals method but missing hashCode method");
				}
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
