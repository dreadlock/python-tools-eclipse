#!/usr/bin/env python

"""Parse the given Python file and dump the ast tree to stdout as JSON."""

import ast
import json
import sys


def printModule(name, module):
  children = []
  m = {'file': name, 'children': children, 'type': 'module'}
  
  for node in module.body:
    if isinstance(node, ast.ClassDef):
      children.append(handleClassDef(node))
    elif isinstance(node, ast.FunctionDef):
      children.append(handleFunctionDef(node))
    elif isinstance(node, ast.Import):
      children.append(handleImport(node))
    elif isinstance(node, ast.Assign):
      n = handleAssign(node)
      if n:
        children.append(n)
    elif isinstance(node, ast.If):
      n = handleIf(node)
      if n:
        children.append(n)
    #else:
    #  print '%s%s' % (indent, type(node))

  print json.dumps(m, separators=(',',':'))
  

def handleClassDef(node):
  # identifier name, expr* bases, stmt* body, expr* decorator_list
  children = []
  c = {'type': 'class', 'children': children, 'start': location(node),
    'docs': docFor(node)}
  
  for child in node.body:
    if isinstance(child, ast.FunctionDef):
      children.append(handleFunctionDef(child))
   
  return c


def handleFunctionDef(node):
  # identifier name, arguments args, stmt* body, expr* decorator_list
  func = {'type': 'function', 'name': node.name, 'start': location(node),
    'docs': docFor(node)}
  
  return func


def handleImport(node):
  # Import(alias* names)
  # alias = (identifier name, identifier? asname)
  children = []
  i = {'type': 'import', 'start': location(node), 'children': children}
  
  for child in node.names:
    c = {'name': child.name}
    if child.asname:
      c['asname'] = child.asname
    children.append(c)
    
  return i


def handleAssign(node):
  # Assign(expr* targets, expr value)
  if node.targets and isinstance(node.targets[0], ast.Name):
    nameNode = node.targets[0]
    return {'type': 'assign', 'start': location(node), 'name': nameNode.id}
  return None


def handleIf(node):
  # If(expr test, stmt* body, stmt* orelse)
  #main = "__name__ == '__main__'"
  mainCheck = "Compare(left=Name(id='__name__', ctx=Load()), ops=[Eq()], comparators=[Str(s='__main__')])" # pylint: disable=C0301
  
  if ast.dump(node.test) == mainCheck:
    return {'type':'if', 'name':'__main__', 'start': location(node)}
  return None


def location(identifier):
  return {"line": identifier.lineno, "col": identifier.col_offset}
  
  
def process(filename):
  contents = file(filename).read()
  module = ast.parse(contents, filename)  
  printModule(filename, module)
  
  
def processStdin():  
  contents = sys.stdin.read()
  module = ast.parse(contents, 'stdin')
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
