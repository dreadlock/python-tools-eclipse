#parse the given python file, and dump the ast tree to stdout as JSON

import sys
import ast

myVar = 0

#<_ast.Import object at 0x108e94990>
#<_ast.Assign object at 0x108e94a90>

class foofoo(ast.NodeVisitor):
  def generic_visit(self, node):
    print node
    #ast.NodeVisitor.generic_visit(self, node)
  def visit_Module(self, node):
    print '{'
    ast.NodeVisitor.generic_visit(self, node)
    print '}'
  def visit_Assign(self, _):
    print '{assign}'
  def visit_If(self, _):
    print '{if}'
  def visit_Import(self, _):
    print '{import}'
  def visit_FunctionDef(self, node):
    print '{function=' + node.name + '}'
    #ast.NodeVisitor.generic_visit(self, node)
  def visit_Attribute(self, node):
    print '{attribute=%s' % node.name
  def visit_ClassDef(self, node):
    print '{class=' + node.name + ','
    ast.NodeVisitor.generic_visit(self, node)
    print '}'

#FunctionDef

def main(filename):
  filecontents = file(filename).read()
  
  #print filename
  
  #astNode = ast.parse("print 'hello!'\nprint 'foo'") #, '<string>', 'exec')
  astNode = ast.parse(filecontents, filename) #, '<string>', 'exec')
  #print ast.dump(astNode, include_attributes=True)
  
  myFoofoo = foofoo()
  myFoofoo.visit(astNode)
  
if __name__ == '__main__':
  #TODO: change to using argv[1]
  sys.exit(main(sys.argv[0]))
