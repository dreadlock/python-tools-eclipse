#parse the given python file, and dump the ast tree to stdout as JSON

import ast
import json
import sys

#from pprint import pprint

myVar = 0

class MyClass:
  def __init__(self):
    print self
  def FunctionA(self, one):
    print self + one
  def FunctionB(self, two):
    print self + two
    

# TODO: clean up the printing; better json output, using the json library
# TODO: if __name__ == '__main__':
# TODO: top level assignments?

def printModule(name, module):
  indent = '  '
  
  print '{"file":"%s","module": [' % name
  
  for child in module.body:
    if isinstance(child, ast.ClassDef):
      printClassDef(child, indent)
    elif isinstance(child, ast.FunctionDef):
      printFunctionDef(child, indent)
    elif isinstance(child, ast.Import):
      printImport(child, indent)
    #elif isinstance(child, ast.If):
    #  printIf(child, indent)
    #else:
    #  print '%s%s' % (indent, type(child))
      
  print ']}'


def printClassDef(node, indent):
  # identifier name, expr* bases, stmt* body, expr* decorator_list
  print '%s{"class":{"name":"%s","location":%s,"doc":%s,"children":[' % (
      indent, node.name, location(node), docFor(node))
  
  for child in node.body:
    if isinstance(child, ast.FunctionDef):
      printFunctionDef(child, indent + '  ')
    
  print '%s]}},' % indent


def printFunctionDef(node, indent):
  # identifier name, arguments args, stmt* body, expr* decorator_list
  #pprint (dir(node.name))
  print '%s{"function":{"name":"%s","location":%s,"doc":%s}},' % (
    indent, node.name, location(node), docFor(node))


def printImport(node, indent):
  # Import(alias* names)
  # alias = (identifier name, identifier? asname)
  #TODO: process the names list
  print '%s{"import":{"name":"%s","location":%s}},' % (
    indent, node.names[0].name, location(node))


#def printIf(node, indent):
#  # If(expr test, stmt* body, stmt* orelse)
#  main = "__name__ == '__main__'"
#  mainCheck = "Compare(left=Name(id='__name__', ctx=Load()), ops=[Eq()],"
#" comparators=[Str(s='__main__')])";
#  
#  if ast.dump(node.test) is mainCheck:
#    print '%s{"if":{"name":"%s","location":%s}},' % (
#      indent, main, location(node))


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
  
  
def docFor(node):
  return json.JSONEncoder().encode(ast.get_docstring(node, clean=True))
  
    
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
