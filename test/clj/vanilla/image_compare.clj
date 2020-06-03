(ns vanilla.image-compare
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log])
  (:import (java.awt.image BufferedImage)
           (javax.imageio ImageIO)
           (java.io File)))




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; convenience functions
;

(defn- load-image
  "loads a PGN file of the given filename at the path provided

  * path     - relative path to the file

  * filename - base name of the file, not including path or extension (assumed to be PNG)

  * returns a new'd BufferedImage, ready for further processing

  *Assumptions/Errors*

  1. Assumes the path exists or will throw a Java Exception, specifically:

      Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
      Can't read input file!

  2. Assumes the path ends with the correct path separator, usually '/' or will throw:

         Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
         Can't read input file!

  3. Assumes the file is a PNG (portable network graphics). IIOException again.

  "
  [path filename]
  (ImageIO/read (File. (str path filename ".png"))))



(defn- compare-name
  "create a valid filename to use for the 'result' image

  * path     - relative path to the location ofr storing the new file

  * filename - base name of the file, not including path or extension (assumed to be PNG)

  * returns string containing a valid filename


  *Assumptions/Errors*

  1. Assumes the path exists or will throw a Java Exception, specifically:

          Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
          Can't read input file!

  2. Assumes the path ends with the correct path separator, usually '/' or will throw:

          Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
          Can't read input file!

  3. Assumes the file is a PNG (portable network graphics). IIOException again.

  "
  [path filename]
  (str path filename ".png"))



; TODO: save-image-diffs is TOO SLOW to be practical in Clojure - use Java directly
;
(defn save-image-diffs
  "compare 2 images and save a new image that just includes the differences in pixels

  * m-path - path to the 'master' image, the one presumed to be correct

  * c-path - path to the 'candidate', the image you are testing

  * r-path - the location to write out the resulting image, which will be the visual 'difference'
             between the candidate and the master, this image will be the same size as the 'master'

  * filename - name of the file, without path or extension, for both the sources and the
               result of the comparison. If this file exists in r-path, it will be overwritten

  * returns true

  This function uses Java-interop to access the ImageIO and BufferedImage classes

  Any pixels that are different will trigger writing the 'candidate' pixel to the result

  NOTE: we need to use (doseq...) here instead of the more usual (for ..) because
  'for' is lazy and doesn't handle side-effects (i.e., mutability, like atoms or Java objects). In this case
  (doseq...) works the same since it can support multiple bindings, but side-effecting function in the
  body are okay. See https://clojuredocs.org/clojure.core/doseq

  NOTE 2: we could use (for...) by wrapping it in a (doall...) which will force evaluation of the
  side-effecting code, but (doseq...) is probably more idiomatic in this case.

  *Assumptions/Errors*

  1. Assumes all three paths exist (m-path, c-path and r-path) or will throw a Java Exception,
  specifically:

         Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
         Can't read input file!

  2. Assumes a file with the name <filename>.png exists in both m-path and c-path or it will throw:

         Execution error (IIOException) at javax.imageio.ImageIO/read (ImageIO.java:1302).
         Can't read input file!

  3. Assumes all files a PNG (portable network graphics). IIOException again.

  4. Both master and candidate images are the same size in both dimension, otherwise throws:

         Execution error (ArrayIndexOutOfBoundsException) at sun.awt.image.ByteInterleavedRaster/getDataElements (ByteInterleavedRaster.java:318).
         Coordinate out of bounds!

  "

  [m-path c-path r-path filename]

  (let [master (load-image m-path filename)
        candidate (load-image c-path filename)
        newName (compare-name r-path filename)
        result (BufferedImage. (.getWidth master) (.getHeight master) (.getType master))]
    (doseq [x (range (.getWidth master))
            y (range (.getHeight master))]
      (let [t (.getRGB candidate x y)
            diff (Math/abs (- t (.getRGB master x y)))]
        (if (not= 0 diff)
          (.setRGB result x y t))))
    (ImageIO/write result "png" (File. newName))))




(comment

  (def filename "post-login")

  (let [master (load-image "etaoin/master/" filename)
        candidate (load-image "etaoin/test/" filename)
        newName (compare-name "etaoin/result/" filename)
        result (BufferedImage. (.getWidth master) (.getHeight master) (.getType master))]
    (doseq [x (range (.getWidth master))
            y (range (.getHeight master))]
      (let [t (.getRGB candidate x y)
            diff (Math/abs (- t (.getRGB master x y)))]
        (if (not= 0 diff)
          (.setRGB result x y t))))
    (ImageIO/write result "png" (File. newName)))

  (save-image-diffs "etaoin/master/" "etaoin/test/" "etaoin/result/" "post-login")



  (let [master (load-image "etaoin/master/" filename)
        candidate (load-image "etaoin/test/" filename)
        newName (compare-name "etaoin/result/" filename)
        result (BufferedImage. (.getWidth master) (.getHeight master) (.getType master))]
    (doall
      (for [x (range (.getWidth master))
            y (range (.getHeight master))]
        (let [t (.getRGB candidate x y)
              diff (Math/abs (- t (.getRGB master x y)))]
          (if (not= 0 diff)
            (.setRGB result x y t)))))
    (ImageIO/write result "png" (File. newName)))


  ; some working examples

  ; 1. candidate has extra data
  (save-image-diffs "etaoin/master/" "etaoin/test/"
    "etaoin/result/" "post-login")

  ; 2. candidate is missing some data
  (save-image-diffs "etaoin/master/" "etaoin/test/"
    "etaoin/result/" "post-login-less-data")


  ; some non-working examples

  ; a. throws an exception because m-path doesn't exist
  (save-image-diffs "bad/path/" "etaoin/test/"
    "etaoin/result/" "post-login")

  ; b. throws an exception because the file "missing.png" doesn't exist anywhere
  (save-image-diffs "etaoin/master/" "etaoin/test/"
    "etaoin/result/" "missing")

  ; c. throws exception because master and candidate are different sizes (candidate smaller)
  (save-image-diffs "etaoin/master/" "etaoin/test/"
    "etaoin/result/" "post-login-diff-sizes")

  ; d. check out errors in the support functions
  (load-image "missing-path/" "post-login")
  (load-image "etaoin/master" "post-login")

  ())


