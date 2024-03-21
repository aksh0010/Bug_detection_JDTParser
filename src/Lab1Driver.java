import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;

public class Lab1Driver {

	private static boolean flag_has_equals=false;
	private static boolean flag_has_hashCode=false;
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

			@Override
			public boolean visit(MethodDeclaration node) {
				// TODO Auto-generated method stub
				
				if(node.getName().getIdentifier().equals("equals")) {
					flag_has_equals=true;
				}
				if(node.getName().getIdentifier().equals("hashCode")) {
					flag_has_hashCode=true;
				}
			/*	System.out.println("Name of the node :"+node.getName().getIdentifier()+" ReturnType :"+node.getReturnType2());
				
				//Start pos
				System.out.println(cu.getLineNumber(node.getStartPosition()));
				
				// End pos = start + length
				System.out.println(cu.getLineNumber(node.getStartPosition()+node.getLength()));
				*/
				//Getting param
				/*for (Object obj :node.parameters()) {
					SingleVariableDeclaration svd =(SingleVariableDeclaration)obj;
					
					System.out.println("Parameter type :"+ svd.getType()+" ParamName: "+svd.getName().getIdentifier());
					
				}*/
				
//				System.out.println("MecodDeclaration: "+node.getName().getFullyQualifiedName());
				
				return true;
			}

			@Override
			public boolean visit(MethodInvocation node) {
				System.out.println("Name of the method call: "+node.getName().getIdentifier() +" Receiver expression: "+node.getExpression());
				return true;
			}

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
						      
			                    
			                }
			            }
			        }
			    }
			    return true;
			}

			
			
		}
				);
		
		//we need to parse the content
		
	}
	private boolean isConstantExpression(Expression expression) {
	    return expression instanceof NumberLiteral ||
	           expression instanceof BooleanLiteral ||
	           expression instanceof CharacterLiteral ||
	           expression instanceof StringLiteral ||
	           expression instanceof NullLiteral;
	}
	public static void main(String[] args) {
		
		Lab1Driver driver = new Lab1Driver();
		try {
			driver.run();
			
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
