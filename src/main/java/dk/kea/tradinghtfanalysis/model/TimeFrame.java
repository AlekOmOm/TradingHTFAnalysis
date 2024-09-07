package dk.kea.tradinghtfanalysis.model;

public enum TimeFrame {
    ONE_MINUTE,         // 0 ordinal
    FIVE_MINUTES,      // 1 ordinal
    FIFTEEN_MINUTES,   // 2 ordinal

    ONE_HOUR,
    SIX_HOURS,

    ONE_DAY,

    ONE_WEEK,

    ONE_MONTH,
    THREE_MONTHS;


    @Override
    public String toString() {
        return switch (this) {
            case ONE_MINUTE -> "1 minute";
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
