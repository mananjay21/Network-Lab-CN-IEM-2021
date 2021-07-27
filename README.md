# Network Lab CN IEM 2021

Demo Instructions 2021
Demo Connection Details Submission Form

Demo Schedule 
* you may deploy at the start of your slot at 1pm, 2pm or 3pm
* only the designated team should access the machine during the allocated slot
* inform your Examiner that your deployment is ready for demo

Come to show your original work, i.e. not code taken from another team.

0. Check in your code to GitHub, and create a release with the binaries - we did this for the SWE Lab Project.

1. Deploy your binaries on the designated machines (see detailed instructions below).

2. Connect to your allocated 2 machines (Server and Client) via AnyDesk during your timeslot.

3. Execute the demo according to the demo sequence specified below.

4. Team members will participate via GoogleMeet.  The ServerMachine screen will be shared; the ServerMachine will be connected to the ClientMachine via AnyDesk.


Deployment
Create a folder with your team name under /home/iem/NetworkLabDemo (NetworkLabDemo has already been created) e.g. /home/iem/NetworkLabDemo/myteam or ~/NetworkLabDemo/myteam
Using the Anydesk file transfer functionality, copy the binaries - FServer.class and FServerM.class to your designated server machine, FClient.class to your designated client machine.  The lab machines have JRE 11.
The input files - demoText.html, demoImg.png, demoPDF.pdf, demoSound.mp3 - are provided in /home/iem/input.
AnyDesk password = p@ssword

Ubuntu OS username / password = iem / 123


Round1 - Stop-And-Wait Demo Sequence
Note: the same FClient is to be used for the entire demo.

0.  Adhere strictly to the RDT Protocol Requirements.

1.  Run Stop-and-Wait ARQ with Lost Frame and Lost Acknowledgement, to generate the given sample output given in Project page. 

Start the FServer and FClient with the following command line parameters.  The FServer reads the input file from its current working directory; the FClient writes the output file into its current working directory.
java FServer port
java FClient server-ip-address port filename

Transfer demoText.html.  Do a diff with demoText1.html on the client machine.
Transfer demoImg.png, demoPDF.pdf, demoSound.mp3.  Do a cmp on the client machine.

2.  Run the multithreaded FServerM.  
Run 3 instances of FClient simultaneously. 
Transfer demoText.html, demoImg.png, demoPDF.pdf, demoSound.mp3.
Do a cmp on the client machine. 

Round2 - Stop-And-Wait Demo Sequence

Run FServer to execute Stop-and-Wait ARQ with Lost Frame and Lost Acknowledgement, allowing up to 3 frames to be specified.  Run FClient. , allowing up to 3 frames to be specified.

java FServer port 2 4 7
java FClient server-ip-address port filename 5 6 8

You shall perform the file transfer with the Server / Client implemented by another team across the CSE Lab LAN.
