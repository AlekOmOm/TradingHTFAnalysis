package dk.kea.tradinghtfanalysis.model;

public enum TimeFrame {
    FIVE_MINUTES,       // 0 ordinal
    FIFTEEN_MINUTES,    // 1 ordinal

    ONE_HOUR,           // 2 ordinal
    SIX_HOURS,          // 3 ordinal

    ONE_DAY,            // 4 ordinal

    ONE_WEEK,           // 5 ordinal

    ONE_MONTH,          // 6 ordinal
    THREE_MONTHS;       // 7 ordinal

    @Override
    public String toString() {
        return switch (this) {
            case FIVE_MINUTES -> "5 minutes";
            case FIFTEEN_MINUTES -> "15 minutes";

            case ONE_HOUR -> "1 hour";
            case SIX_HOURS -> "6 hours";

            case ONE_DAY -> "1 day";

            case ONE_WEEK -> "1 week";

            case ONE_MONTH -> "1 month";
            case THREE_MONTHS -> "3 months";
        };
    }

}
