#include <unistd.h>
#include <sys/io.h>
#include <string.h>
#include <stdio.h>

void
usage (void)
{
  fprintf (stderr, "Hey, let me know if you want to go \"on\" or \"off\"\n");
  exit (1);
}

int
main (int argc, char **argv)
{
  int val;
  if (argv[1] == 0)
    usage();
  if (!strcmp (argv[1], "on"))
    val = 0xff;
  else if (!strcmp (argv[1], "off"))
    val = 0x00;
  else
    usage ();
  if (ioperm (0x378, 4, 1) < 0)
    {
    perror ("Can't get at parallel port");
    exit (1);
    }
  outb(val,0x378);
  exit (0);
}    
