# Kdice-game
Is simpler version of Kdice game http://kdice.com
<br>It is an app with Server and 5 Clients (multithreating on server side, to connect i have used sockets).
<br>You can open all of them on your computer or run server on one computer then clients on others.
<br>To do this you have to change line 22 in Client.java
<br><i>socket = new Socket(address, 4445);</i>
<br>address will be address of your server
<br>
<br>In principle clients are automatic so all work is doing in ServerThread.java and Client only receiving what is happening.
