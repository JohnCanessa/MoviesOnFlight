import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


/**
 * Amazon | OA 2019 | Movies on Flight
 * https://leetcode.com/discuss/interview-question/313719/Amazon-or-Online-Assessment-2019-or-Movies-on-Flight
 */
public class MoviesOnFlight {


    /**
     * Find the pair of movies with the longest total duration and return their indices.
     * If multiple found, return the pair with the longest movie.
     * 
     * Exact match!!!
     */
    static List<Integer> moviesOnFlight0(List<Integer> movieDurations, int flightDuration) {

        // **** sanity checks ****
        if (movieDurations.size() <= 1)
            return Arrays.asList(-1, -1);

        // **** initialization ****
        Map<Integer, List<Integer>> mapDurationID = new HashMap<>();
        int maxD1       = -1;
        int maxD2       = -1;
        int maxNdex1    = -1;
        int maxNdex2    = -1;
        int fd          = flightDuration - 30;          // for ease of use

        // **** populate hash map with duration -> ID in O(n) ****
        for (int i = 0; i < movieDurations.size(); i++) {

            // **** get duration of this movie ****
            int duration = movieDurations.get(i);

            // ???? ????
            // System.out.println("moviesOnFlight0 <<< i: " + i + " duration: " + duration);

            // **** check if we already have this movie ****
            List<Integer> lst = mapDurationID.get(duration);

            // **** create new list (if needed) ****
            if (lst == null)
                lst = new ArrayList<Integer>();

            // **** map duration and ID ****
            lst.add(i);
            mapDurationID.put(duration, lst);
        }

        // ???? ????
        System.out.println("moviesOnFlight0 <<< mapDurationID: " + mapDurationID.toString());

        // **** traverse list looking for pair(s) of movies ****
        for (int i = 0; i < movieDurations.size(); i++) {
            
            // **** get duration for this movie ****
            int d1 = movieDurations.get(i);

            // ???? ????
            // System.out.println("moviesOnFlight0 <<< i: " + i + " d1: " + d1);

            // **** compute duration for second movie ****
            int d2 = fd - d1;

            // ???? ????
            // System.out.println("moviesOnFlight0 <<< d2: " + d2);

            // **** look for movie with d1 ****
            List<Integer> lst1 = mapDurationID.get(d1);

            // **** look for movie with d2 ****
            List<Integer> lst2 = mapDurationID.get(d2);

            // **** check if SAME durations (in same list) have same index ****
            if (lst1.equals(lst2)) {

                // ???? ????
                // System.out.println("moviesOnFlight0 <<< lst2: " + lst2.toString());

                // **** check if indices are the same ****
                if (lst2.size() <= 1)
                    continue;
            }

            // **** found ID with d2 ****
            if (lst2 != null) {
                
                // ???? ????
                // System.out.println("moviesOnFlight0 <<< lst: " + lst2.toString());

                // **** check if this pair has the longest movie ****
                if (d1 > maxD1 || d2 > maxD2) {

                    // **** update first movie ****
                    maxD1       = d1;
                    maxNdex1    = i;

                    // **** update second movie ****
                    maxD2 = d2;
                    if (mapDurationID.get(d2).size() <= 1)
                        maxNdex2 = mapDurationID.get(d2).get(0);
                    else
                        maxNdex2 = mapDurationID.get(d2).get(1);
                }
            }
        }

        // ???? ????
        // System.out.println("moviesOnFlight0 <<< maxD1: " + maxD1 + " maxD2: " + maxD2);

        // **** return movie pair ****
        if (maxD1 == -1 || maxD2 == -1)
            return Arrays.asList(-1, -1);
        else
            return Arrays.asList(maxNdex1, maxNdex2);
    }


    /**
     * Find the pair of movies with the longest total duration and return they indices.
     * If multiple found, return the pair with the longest movie.
     * 
     * Less than or equal match !!!
     */
    static List<Integer> moviesOnFlight(List<Integer> movieDurations, int flightDuration) {

        // **** sanity checks ****
        if (movieDurations.size() <= 1)
            return Arrays.asList(-1, -1);

        // **** initialization ****
        int fd  = flightDuration - 30;          // for ease of use
        int m1  = -1;
        int m2  = -1;
        int dev = Integer.MAX_VALUE;
        int i   = 0;
        int j   = movieDurations.size() - 1;

        // **** clone the movie durations ****
        List<Integer> clone = new ArrayList<Integer>(movieDurations);   

        // **** sort clone of movie durations O(nlog(n)****
        Collections.sort(clone);

        // ???? ????
        System.out.println("moviesOnFlight <<< clone: " + clone);

        // **** loop looking for pair closest to d ****
        while (i <= movieDurations.size() - 1 && j >= 0 && i < j) {

            // ???? ????
            // System.out.println("moviesOnFlight <<< i: " + i + " j: " + j);

            // **** compute time for both movies ****
            int time = clone.get(i) + clone.get(j);

            // ???? ????
            // System.out.println("moviesOnFlight <<< time: " + time);

            // **** compute time difference ****
            int diff = fd - time;

            // ???? ????
            //System.out.println("moviesOnFlight <<< diff: " + diff);

            // **** time exceeded :o( ****
            if (diff < 0) {
                j--;
            }

            // **** time less than flight time - 30 minutes :o) ****
            else if (diff > 0) {

                // **** check if we already have a pair with same difference ****
                if (diff == dev) {

                    // **** ****
                    if (clone.get(m1) < clone.get(i) ||
                        clone.get(m2) < clone.get(j)) {
                            dev = diff;
                            m1  = i;
                            m2  = j;
                        }
                }

                // **** check if these movies are closer to our goal ****
                else {

                    // **** update the remain and save these movies ****
                    if (diff < dev) {
                        dev = diff;
                        m1  = i;
                        m2  = j;
                    }
                }

                // ???? ????
                // System.out.println("moviesOnFlight <<< dev: " + dev);

                // **** ****
                i++;
            }

            // **** found movies that meet exact flight time - 30 :o) ****
            else {

                // // ???? ????
                // System.out.println("moviesOnFlight <<< i: " + i + " j: " + j);
                // System.out.println("moviesOnFlight <<< m1: " + m1 + " m2: " + m2);

                // **** ****
                int d1 = clone.get(i);
                int d2 = clone.get(j);

                // // ???? ????
                // System.out.println("moviesOnFlight <<< d1: " + d1 + " d2: " + d2);

                // **** map movies to initial locations ****
                m1 = movieDurations.indexOf(d1);
                m2 = movieDurations.lastIndexOf(d2);

                // **** return movie pair ****
                return Arrays.asList(m1, m2);
            }
        }

        // ???? ????
        // System.out.println("moviesOnFlight <<< m1: " + m1 + " m2: " + m2);

        // **** check if movie pair not found ****
        if (m1 == -1 && m2 == -1)
            return Arrays.asList(-1, -1);

        // **** map movies to initial locations ****
        m1 = movieDurations.indexOf(clone.get(m1));
        m2 = movieDurations.indexOf(clone.get(m2));

        // **** return movie pair ****
        return Arrays.asList(m1, m2);
    }


    /**
     * Test scaffold.
     * 
     * NOT PART OF SOLUTION
     * 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        // **** initialization ****
        List<Integer> movies = null;

        // **** open buffered reader ****
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        // **** create and populate list of movie durations ****
        List<Integer> movieDurations =  Arrays.stream(br.readLine().trim().split(","))
                                            .map(s -> s.replace(" ", ""))
                                            .map(Integer::parseInt)
                                            .collect(toList());
        
        // **** read and extract flight duration (in minutes) ****
        int flightDuration = Integer.parseInt(br.readLine().trim());

        // **** close buffered reader ****
        br.close();

        // ???? ????
        System.out.println("main <<< movieDurations: " + movieDurations.toString());
        System.out.println("main <<< flightDuration: " + flightDuration + " (" + 
                            (flightDuration - 30) + ")");

        // **** select movies ****
        movies = moviesOnFlight0(movieDurations, flightDuration);

        // ???? ????
        if (movies.get(0) == -1 && movies.get(1) == -1)
            System.out.println("main <<< exact: " + movies.toString()); 
        else
            System.out.println("main <<< exact: " + movies.toString() + " (" + 
             (movieDurations.get(movies.get(0)) + movieDurations.get(movies.get(1))) + ")");


        // **** select movies ****
        movies = moviesOnFlight(movieDurations, flightDuration);

        // ???? ????
        if (movies.get(0) == -1 && movies.get(1) == -1)
            System.out.println("main <<< aprox: " + movies.toString()); 
        else
            System.out.println("main <<< aprox: " + movies.toString() + " (" + 
                (movieDurations.get(movies.get(0)) + movieDurations.get(movies.get(1))) + ")");
    }

}