# MR-Graphics

Fullscreen graphics view for Mixed-Reality soccer scenario.

## DEPENDENCIES

* mrServer build files

## INSTALLATION
Firts you need the mrServer (https://github.com/NorthernStars/mrserver) installed and build.

Now clone this repository next to the mrServer repository.

     git clone https://github.com/NorthernStars/MR-Graphics.git
     
Change into the cloned repository and build it using ant

     cd MR-Graphics
     ant
     
Run the MR-Graphics (also see command-line options below):

     java -jar mrgraphics.jar

That's all!


## Command-line arguments
MR-Graphics supports following command-line arguments:

     -s, --server		Remote server IP (default: 127.0.0.1)
     -p, --port			Remote server port (default: 9060)
     -w, --width		Soccer field window width in px, if no fullscreen
     -h, --height		Soccer field window height in px, if no fullscreen
     -l, --left			Soccer field window position left in px, if no fullscreen
     -t, --top			Soccer field window position top in px, if no fullscreen
     -f, --fullscreen		Set to enable fullscreen. Will use exlusive fullscreen if supported, otherwise borderless window.
     -d, --display		Number of display/monitor where to show the soccer field.
     -a, --autoconnect		Auto connect to remote server
     -q, --quiet		Starts without control user interface, includes autoconnect.
