# deepunzip

A simple tool to perform recursive unzipping of a ZIP archive.

In case that the archive contains other ZIP archives,
they are unpacked, too, to the sub-directories having
the same names as the original zip archives.

This tool is sometimes handy to work with the output
of "support" data collectors, which tend to put ZIP archives
one inside another many times.
