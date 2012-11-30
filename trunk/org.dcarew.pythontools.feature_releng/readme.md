In order to create a build of Python Tools:
-------------

From this directory (org.dcarew.pythontools.feature_releng) run ant. It'll load build.xml by default
and run the default (desktop_build) target.

The first build will take a while as a 3.7 version of Eclipse is downloaded. Subsequent builds will
be faster (< 1 minute).

The build will ultimately create a zip file containing an update site for the latest version of
Python Tools. It'll be in your build output directory, and named pythontools-updatesite-vXXX.zip.

When reving the version number of Python Tools:
-------------

Change the version number in org.dcarew.pythontools.feature/feature.xml. On the feature's 'Plugins' 
tab, run the command Versions... > Force feature version into plugin manifests. This will update the
plugin versions to match the feature.

Run a build (org.dcarew.pythontools.feature_releng/build.xml).

Unzip the contents of the pythontools-updatesite-vXXX.zip build result into the 
org.dcarew.pythontools.updatesite directory.

Commit all the feaure.xml, plugin.xml, and update site changes to svn.

Cloudbees:
-------------
We have a FOOS account at cloudbees.com - a continuous build service. The status of builds can be
seen here:

  https://devoncarew.ci.cloudbees.com/job/PythonTools/

