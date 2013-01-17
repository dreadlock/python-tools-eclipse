#!/usr/bin/env python

"""A Python debugger."""

import bdb
import json
import optparse
import os
import socket
import sys

dbg = None
mainScript = None

def ExecuteCommand(command):
  cmd = command['command']
  
  if cmd == 'run':
    dbg.run('execfile(%r)' % mainScript)
    #TODO: respond to the command -
  else:
    print 'unhandled command: %s' % command['id']
  

def EnterDispatchLoop(clientsocket):
  f = clientsocket.makefile()
  
  for line in f:
    line = line.strip()
    command = json.loads(line)
    ExecuteCommand(command)
    
  return 0

def StartDebugger(port, script):
  global dbg
  global mainScript
  mainScript = script
  
  # patch up argv
  # TODO: remove any --port option as well
  del sys.argv[0]
  sys.path[0] = os.path.dirname(script)
  
  # wait for connection
  serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  serversocket.bind(('', port))
  serversocket.listen(0)
  (clientsocket, _) = serversocket.accept()
  serversocket.close()

  dbg = bdb.Bdb()
    
  return EnterDispatchLoop(clientsocket)
  

def BuildOptions():
  options = optparse.OptionParser(usage="usage: %prog [options] <script>")
  options.add_option("-p", "--port", help='tcp port', default=9200, type='int')
  return options


def Main():
  parser = BuildOptions()
  (options, args) = parser.parse_args()
  
  if len(args)  == 0:
    parser.print_help()
    return 1
  
  if not os.path.exists(args[0]):
    print 'Error: %s does not exist' % args[0]
    sys.exit(1)
  
  return StartDebugger(options.port, args[0])
  
    
if __name__ == '__main__':
  sys.exit(Main())
