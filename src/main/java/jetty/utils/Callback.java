package jetty.utils;

/**
 * Created by yaozb on 15-4-18.
 */
public interface Callback {
    /**
     * Callback invoked when the operation completes.
     */
    public abstract void succeeded();

    /**
     * Callback invoked when the operation fails.
     */
    public void failed(Throwable x);

    /**
     */
    public static class Adapter implements Callback
    {
        /**
         * Instance of Adapter that can be used when the callback methods need an empty
         * implementation without incurring in the cost of allocating a new Adapter object.
         */
        public static final Adapter INSTANCE = new Adapter();

        @Override
        public void succeeded()
        {
        }

        @Override
        public void failed(Throwable x)
        {
        }
    }
}
