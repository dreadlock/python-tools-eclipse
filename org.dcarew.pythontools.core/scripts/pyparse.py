#parse the given python file, and dump the ast tree to stdout as JSON

import sys
import ast

myVar = 0

class MyClass:
  def __init__(self):
    print self
  def FunctionA(self, one):
    print self + one
  def FunctionB(self, two):
    print self + two
    

# TODO: clean up the printing

def printModule(name, module):
  indent = '  '
  
  print '{"module": ['
  
  for child in module.body:
    if isinstance(child, ast.ClassDef):
      printClassDef(child, indent)
      print ','
    elif isinstance(child, ast.FunctionDef):
      printFunctionDef(child, indent)
      print ','
    elif isinstance(child, ast.Import):
      printImport(child, indent)
      print ','
    #else:
    #  print '%s%s' % (indent, type(child))
      
  print ']}'


def printClassDef(node, indent):
  # identifier name, expr* bases, stmt* body, expr* decorator_list
  print '%s{"class":{"name":"%s","location":%s,"children":[' % (
      indent, node.name, location(node))
  
  for child in node.body:
    if isinstance(child, ast.FunctionDef):
      printFunctionDef(child, indent + '  ')
      print ','
    
  print '%s]}}' % indent


def printFunctionDef(node, indent):
  # identifier name, arguments args, stmt* body, expr* decorator_list
  print '%s{"function":{"name":"%s","location":%s}}' % (
    indent, node.name, location(node))


def printImport(node, indent):
  # alias* names
  # alias = (identifier name, identifier? asname)
  # TODO:
  print '%s{"import":"foo"}' % (indent)


def location(identifier):
  return '{"lineno":%i,"col_offset":%i}' % (
    identifier.lineno, identifier.col_offset)
  
  
def process(filename):
  contents = file(filename).read()
  module = ast.parse(contents, filename) #, '<string>', 'exec')  
  printModule(filename, module)
  
  
def processStdin():  
  contents = sys.stdin.read()
  module = ast.parse(contents, 'stdin') #, '<string>', 'exec')
  printModule('stdin', module)
  
  
if __name__ == '__main__':
  # if sys args are given, read from and process each file
  # else read from stdin
  fileList = sys.argv[1:]
  if fileList:
    for f in fileList:
      process(f)
  else:
    processStdin()
    
  sys.exit(0)
