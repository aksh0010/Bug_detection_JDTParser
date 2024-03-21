import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;

public class Lab1Driver {

	public void run() throws IOException {
		
		//we need to read the content of the file
		String content = FileUtils.readFileToString(new File("C:\\Users\\akshr\\eclipse-workspace\\Comp4110_Assignment3\\src\\Person.java"));
		
		//we need to create a parser object
		ASTParser parser = ASTParser.newParser(AST.JLS21);
		
		//we need to give the content to the parser
		parser.setSource(content.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {

			@Override
			public boolean visit(MethodDeclaration node) {
				// TODO Auto-generated method stub
				
				System.out.println("Name :"+node.getName().getIdentifier()+" ReturnType :"+node.getReturnType2());
				
				//Start pos
				System.out.println(cu.getLineNumber(node.getStartPosition()));
				
				// End pos = start + length
				System.out.println(cu.getLineNumber(node.getStartPosition()+node.getLength()));
				
				//Getting param
				for (Object obj :node.parameters()) {
					SingleVariableDeclaration svd =(SingleVariableDeclaration)obj;
					
					System.out.println("Parameter type :"+ svd.getType()+" ParamName: "+svd.getName().getIdentifier());
					
				}
				
				System.out.println("MecodDeclaration: "+node.getName().getFullyQualifiedName());
				
				return true;
			}

			@Override
			public boolean visit(MethodInvocation node) {
				// TODO Auto-generated method stub
				
				System.out.println("Name of the method call: "+node.getName().getIdentifier() +" Receiver expression: "+node.getExpression());
				return true;
			}
			
			
		}
				);
		
		//we need to parse the content
		
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Lab1Driver driver = new Lab1Driver();
		try {
			driver.run();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
