Blackspirit Graphics 2.0.1
==========================

For information on changes and versions or further documentation please refer to 
'http://www.blackspirit.ch'.


Release Notes
-------------
Release Date: 2011-09-28

Bugfix release with minor changes.

Changes:

* Added transformatin stack (push/pop) for managing tranformation state
* Max supported image width/height/pixels can be retrieved from ImageFactory
* Only images supported by Java ImageIO can be loaded (TGA only with ImageIO plugin)

Known bugs:

* Points not drawn on some systems (when glPointSize is set to 1)

Fixed bugs:

* Animation repetition bug in AnimationImpl fixed
* Not using PBuffer with Intel graphics cards to properly support image drawing.


Requirements
------------
Java version 1.5 or newer.

Blackspirit Graphics requires two third-party libraries.

Vecmath:

A binary version of vecmath is bundled with every release and must be included in the classpath.

JOGL 1.1.1:

The JOGL library is available for various operating systems (including Windows, Linux and MacOSX).
As there is a JOGL package available for each operating system and for webstart applications
JOGL is not bundled with the release packages and must be downloaded from 'http://jogamp.org/deployment/archive/master/jogl-old-1.1.1/'.

The Jar archives from the JOGL distribution must be included in the classpath and
the native libraries (.dll or .so) must be made available for the application.
In Eclipse a native library path can be defined for each Jar archive.

Please notice that the Gluegen library (including native libraries) coming with JOGL 
is also needed.


Webstart
--------
A signed version of BSGraphics.jar and bsgraphics.jnlp (changes needed) defining the extension are 
included with the distribution and can be used for webstart applications.

Have a look at the jnlp files of the demos on 'http://www.blackspirit.ch' for an example of
how to set up a webstart application using Blackspirit Graphics.
