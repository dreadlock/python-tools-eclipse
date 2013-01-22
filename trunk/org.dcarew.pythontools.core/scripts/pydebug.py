#!/usr/bin/env python

"""A Python debugger."""

import bdb
import json
import optparse
import os
import socket
import sys
import threading

dbg = None
main_script = None
socket_file = None
port = None
condition = None

def ExecuteCommand(command):
  cmd_id = command['id']
  cmd_name = command['command']
  
  if cmd_name == 'run':
    SendResult(cmd_id)
    
    # start the user's application running
    condition.acquire()
    condition.notify()
    condition.release()
  elif cmd_name == 'version':
    SendResult(cmd_id, result=sys.version)
  else:
    print 'unhandled command: %s %s' % (cmd_name, cmd_id)
  

def _StartDbgThread():
  # wait for connection
  serversocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  serversocket.bind(('', port))
  serversocket.listen(0)
  (clientsocket, _) = serversocket.accept()
  serversocket.close()

  EnterDispatchLoop(clientsocket)
    
  
def SendResult(cmd_id, result=None):
  if not result:
    data = '{"id":%s}\n' % cmd_id    
  else:
    data = '{"id":%s,"result":"%s"}\n' % (cmd_id, result)
  socket_file.write(data)
  socket_file.flush()
  
  
def EnterDispatchLoop(clientsocket):
  global socket_file
  
  socket_file = clientsocket.makefile()
  
  for line in socket_file:
    line = line.strip()
    command = json.loads(line)
    ExecuteCommand(command)
    
  return 0


def StartDebugger():
  global dbg
  global condition
  
  # patch up argv
  # TODO: remove any --port option as well
  del sys.argv[0]
  sys.path[0] = os.path.dirname(main_script)
  
  dbg = bdb.Bdb()
  
  condition = threading.Condition()
  condition.acquire()
  
  # start a separate thread for the debugger
  dbg_thread = threading.Thread(target=_StartDbgThread)
  dbg_thread.daemon = True
  dbg_thread.start()
  
  condition.wait()
  condition.release()
  
  # start the user's application
  dbg.run('execfile(%r)' % main_script)
  
  return 0
  

def BuildOptions():
  options = optparse.OptionParser(usage="usage: %prog [options] <script>")
  options.add_option("-p", "--port", help='tcp port', default=9200, type='int')
  return options


def Main():
  global port
  global main_script
  
  parser = BuildOptions()
  (options, args) = parser.parse_args()
  
  if len(args)  == 0:
    parser.print_help()
    return 1
  
  if not os.path.exists(args[0]):
    print 'Error: %s does not exist' % args[0]
    sys.exit(1)
  
  port = options.port
  main_script = args[0]
  
  return StartDebugger()
  
    
if __name__ == '__main__':
  sys.exit(Main())
