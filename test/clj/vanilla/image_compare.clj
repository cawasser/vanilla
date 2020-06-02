(ns vanilla.image-compare
  (:require [clojure.test :refer :all]
            [clojure.tools.logging :as log])
  (:import (java.awt.image BufferedImage)
           (javax.imageio ImageIO)
           (java.io File)))




(defn load-image [path filename]
  (ImageIO/read (File. (str path filename ".png"))))



(defn compare-name [path filename]
  (str path filename ".png"))



; TODO: save-image-diffs is TOO SLOW to be practical in Clojure - use Java directly
;
(defn save-image-diffs
  "compare 2 images and save a new image that just includes the differences in pixels

  * m-path - path to the 'master' image, the on presumed to be correct

  * c-path - path to the 'candidate', the image you are testing

  * r-path - the location to write out the resulting image, which will be the visual 'difference'
             between the candidate and the master, this image will be the same size as the 'master'

  This function uses Java-interop to access the ImageIO and BufferedImage classes

  Any pixels that are different will trigger writing the 'candidate' pixel to the result

  NOTE: we need to use (doseq...) here instead of the more usual (for ..) because
  'for' is lazy and doesn't handle side-effects (i.e., mutability, like atoms or Java objects). In this case
  (doseq...) works the same since it can support multiple bindings, but side-effecting function in the
  body are okay. See https://clojuredocs.org/clojure.core/doseq

  NOTE 2: we could use (for...) by wrapping it in a (doall...) which will force evaluation of the
  side-effecting code, but (doseq...) is probably more idiomatic in this case."

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

  ())


