
import socket
import sys

if __name__ == '__main__':
  s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
  s.connect(('localhost', 9200))
  
  s.sendall('{"command":"run","id":1234}\n')
  data = s.recv(4096)
  
  print data
  
  s.close()
  
  sys.exit(0)
  