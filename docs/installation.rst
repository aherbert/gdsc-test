.. index:: installation, maven
.. _installation:

Installation
=====

This package is a library to be used used by other Java programs. It is only
necessary to perform an install if you are building other packages that depend
on it.

Installation using Maven
-----

.. highlight:: console

1. Clone the repository::

    git clone https://github.com/aherbert/gdsc-test.git

2. Build the code and install using Maven::

    cd gdsc-test
    mvn install

This will produce a gdsc-test-[module]-[VERSION].jar file in the local Maven
repository. You can now build the other packages that depend on this code.
