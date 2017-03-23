import socket, sys, time
from time import sleep
import  pifacedigitalio as pfio
import time

pfd = pfio.PiFaceDigital()
pfio.init()

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
server_address = ('10.0.0.52', 5050)
s.bind(server_address)
s.setblocking(0)


server_address2 = ('10.0.0.51', 5050)


while(True):

        if(pfd.input_pins[6].value == 1 or pfd.input_pins[7].value == 1):
                
				print ("OMG FLAME DETECTED")
				
				pfio.digital_write(0,1)
				
				notification = "fireFound"
				
				s.sendto(notification.encode('utf-8'), server_address2)
								
				while (True):
					try:
						buf, address = s.recvfrom(port)
						sleep(20)
						break
						
					except socket.error:
						s.sendto(temp.encode('utf-8'), server_address2)			

        else:
                print ("No smoke or flame detected")
                sleep(0.5)


