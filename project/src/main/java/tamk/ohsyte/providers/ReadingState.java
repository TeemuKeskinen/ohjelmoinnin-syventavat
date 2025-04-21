package tamk.ohsyte.providers;

// Enumeration for the state machine
public enum ReadingState {
    DATE {
        @Override
        public ReadingState nextState() {
            return DESCRIPTION;
        }
    },

    DESCRIPTION {
        @Override
        public ReadingState nextState() {
            return CATEGORY;
        }
    },

    CATEGORY {
        @Override
        public ReadingState nextState() {
            return BLANK;
        }
    },

    BLANK {
        @Override
        public ReadingState nextState() {
            return DATE;
        }
    },

    DONE {
        @Override
        public ReadingState nextState() {
            return DATE;
        }
    };

    public abstract ReadingState nextState();
}