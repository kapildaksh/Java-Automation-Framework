NOTES ON DRIVERS:

WINDOWS:
  Should be handled by the current framework (as of Jan. 21, 2015) with no user interaction.

MAC OS:
  Should be handled by the current framework (as of Jan. 21, 2015) with no user interaction.
  An issue was encountered with the chorme driver not being an executable file, but code was 
    implemented that forces the permissions on the file to include execution for all users. 
    This is perfomed each time WebDriverSetup is instantiated.

LINUX OS:
  This was tested against OpenSUSE. Future efforts should include testing against Fedora, 
    Red Hat, Ubuntu, OpenBSD, Debian, Debuan, etc.
  
  OpenSUSE NOTES:
    Older versions of Firefox can be found with binaries at ftp://ftp.mozilla.org/pub/firefox/releases/.
      Location of these files is based on user-preference. By default, Selenium looks for the Firefox 
      executable as /usr/bin/firefox. As a "super user," a soft link can be made to the binaries so that 
      Selenium still uses /usr/bin/firefox, but that command is actually linked to the version of Firefox 
      the user has chosen. Since the soft link must be made manually, this solution would prove 
      problematic when attempting to implement on a multi-node Selenium grid
