package com.example.krisorn.tangwong;


public abstract class FormValidation {
        private boolean result;

        public FormValidation() {
        }

        public abstract boolean validate();

        public boolean isResult() {
            return result;
        }

        public void setResult(boolean result) {
            this.result = result;
        }
}
