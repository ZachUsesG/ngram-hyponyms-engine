package ngrams;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * An object for mapping a year number (e.g. 1996) to numerical data. Provides
 * utility methods useful for data analysis.
 *
 * @author Josh Hug
 */
public class TimeSeries extends TreeMap<Integer, Double> {

    /** If it helps speed up your code, you can assume year arguments to your NGramMap
     * are between 1400 and 2100. We've stored these values as the constants
     * MIN_YEAR and MAX_YEAR here. */
    public static final int MIN_YEAR = 1400;
    public static final int MAX_YEAR = 2100;

    /**
     * Constructs a new empty TimeSeries.
     */
    public TimeSeries() {
        super();
    }

    /**
     * Creates a copy of TS, but only between STARTYEAR and ENDYEAR,
     * inclusive of both end points.
     */
    public TimeSeries(TimeSeries ts, int startYear, int endYear) {
        super();
        for (int year : ts.keySet()) {
            if (year >= startYear && year <= endYear) {
                Double yearValue = ts.get(year);
                this.put(year, yearValue);
            }
        }
    }

    public TimeSeries(TimeSeries timeSeries) {
        super();
        for (int year : timeSeries.keySet()) {
            this.put(year, timeSeries.get(year));
        }
    }

    /**
     *  Returns all years for this time series in ascending order.
     */
    public List<Integer> years() {
        this.keySet();
        List<Integer> yearsList = new ArrayList<Integer>(this.keySet());
        return yearsList;
    }

    /**
     *  Returns all data for this time series. Must correspond to the
     *  order of years().
     */
    public List<Double> data() {
        List<Integer> yearsList = this.years();
        List<Double> dataHolder = new ArrayList<>();
        for (int i = 0; i < yearsList.size(); i++) {
            int year = yearsList.get(i);
            dataHolder.add(this.get(year));
        }
        return dataHolder;
    }

    /**
     * Returns the year-wise sum of this TimeSeries with the given TS. In other words, for
     * each year, sum the data from this TimeSeries with the data from TS. Should return a
     * new TimeSeries (does not modify this TimeSeries).
     *
     * If both TimeSeries don't contain any years, return an empty TimeSeries.
     * If one TimeSeries contains a year that the other one doesn't, the returned TimeSeries
     * should store the value from the TimeSeries that contains that year.
     */
    public TimeSeries plus(TimeSeries ts) {
        TimeSeries result = new TimeSeries();
        for (int year : this.keySet()) {
            if (!ts.containsKey(year)) {
                result.put(year, this.get(year));
            } else {
                result.put(year, this.get(year) + ts.get(year));
            }
        }
        for (int year : ts.keySet()) {
            if (!this.containsKey(year)) {
                result.put(year, ts.get(year));
            }
        }
        return result;
    }

    /**
     * Returns the quotient of the value for each year this TimeSeries divided by the
     * value for the same year in TS. Should return a new TimeSeries (does not modify this
     * TimeSeries).
     *
     * If TS is missing a year that exists in this TimeSeries, throw an
     * IllegalArgumentException.
     * If TS has a year that is not in this TimeSeries, ignore it.
     */
    public TimeSeries dividedBy(TimeSeries ts) {
        TimeSeries dividedResult = new TimeSeries();
        for (int year : this.keySet()) {
            double quotient = safeDivide(this, ts, year);
            dividedResult.put(year, quotient);
        }
        return dividedResult;
    }
    private double safeDivide(TimeSeries this1, TimeSeries ts2, int year) {
        if (!ts2.containsKey(year)) {
            throw new IllegalArgumentException();
        } else {
            return this1.get(year) / ts2.get(year);
        }
    }



}
