package seamfinding;

import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        List<Integer> result = new ArrayList<>();
        double[][] pixels = new double[picture.width()][picture.height()];
        //Set energy of leftmost column in array
        for(int y = 0; y < picture.height(); y++){
            pixels[0][y] = f.apply(picture, 0, y);
        }
        //Set energy of rest of array
        for(int x = 1; x < picture.width(); x++){
            for(int y = 0; y < picture.height(); y++){
                double min = Double.POSITIVE_INFINITY;
                if(y > 0){
                    if(pixels[x-1][y-1] < min){
                        min = pixels[x-1][y-1];
                    }
                }
                if(pixels[x-1][y] < min){
                    min = pixels[x-1][y];
                }
                if(y < picture.height() - 1){
                    if(pixels[x-1][y+1] < min){
                        min = pixels[x-1][y+1];
                    }
                }
                pixels[x][y] = f.apply(picture, x, y) + min;
            }
        }
        //Find start of seam
        int start = 0;
        for(int y = 0; y < picture.height(); y++){
            if(pixels[picture.width()-1][y] < pixels[picture.width()-1][start]){
                start = y;
            }
        }
        result.add(start);
        //Follow the rest of the seam
        for(int x = picture.width() - 2; x >= 0; x--){
            int y = result.get(result.size() - 1);
            int min = y;
            if(y > 0){
                if(pixels[x][y-1] < pixels[x][min]){
                    min = y-1;
                }
            }
            if(y < picture.height() - 1){
                if(pixels[x][y+1] < pixels[x][min]){
                    min = y+1;
                }
            }
            result.add(min);
        }
        Collections.reverse(result);
        return result;
    }
}
