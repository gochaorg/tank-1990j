package xyz.cofe.game.tank.stat;

import xyz.cofe.text.FullDecFormat;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Счетчик для замеров производительности
 */
public class TCounter {
    /**
     * Сколько последних значений хранить
     */
    public final int length;

    /**
     * Замеры - значение в нс.
     * Каждое значение содержит продолжительность - разницу между {@link #start()} и {@link #stop()}
     */
    public final long times[];

    /**
     * Конструктор
     * @param len Сколько значений хранить
     */
    public TCounter(int len){
        if( len<1 )throw new IllegalArgumentException( "len<1" );
        length = len;
        times = new long[len];
        pointer = 0;
    }

    /**
     * Номер очередной записи
     */
    private int pointer;

    /**
     * Время вызова {@link #start()} - нс.
     */
    public long started;

    /**
     * Время вызова {@link #stop()} - нс.
     */
    public long stopped;

    /**
     * Начало очередного замера
     */
    public void start(){
        started = System.nanoTime();
    }

    /**
     * Конец очередного замера
     */
    public void stop(){
        stopped = System.nanoTime();
        times[pointer%length] = stopped - started;
        pointer++;
    }

    public <A> A track( Supplier<A> code ){
        if( code==null )throw new IllegalArgumentException( "code==null" );
        try{
            start();
            return code.get();
        } finally {
            stop();
        }
    }

    public void track( Runnable code ){
        if( code==null )throw new IllegalArgumentException( "code==null" );
        try{
            start();
            code.run();
        } finally {
            stop();
        }
    }

    /**
     * Простая статистика замеров
     */
    public class Echo {
        /**
         * Кол-во замеренных значений, не больше {@link #length}
         */
        public int count;

        /**
         * Суммарное значение
         */
        public double summa;

        /**
         * Минимальное
         */
        public double min;

        /**
         * Максимальное
         */
        public double max;

        /**
         * Среднее
         */
        public double avg;

        /**
         * Отсортированная выборка
         */
        public long[] sorted;

        /**
         * Время статистики - мс {@link System#currentTimeMillis()}
         */
        public long time;
        public Echo(){
            time = System.currentTimeMillis();
            count = Math.min(pointer, length);
            if( count<2 ){
                summa = Double.NaN;
                min = Double.NaN;
                max = Double.NaN;
                avg = Double.NaN;
                sorted = new long[0];
            }else{
                sorted = Arrays.copyOf(times,count);
                Arrays.sort(sorted);
                summa = 0;
                min = Double.MAX_VALUE;
                max = Double.MIN_VALUE;
                avg = Double.NaN;
                for( long t : sorted ){
                    summa += t;
                }
                avg = summa / count;
                min = sorted[0];
                max = sorted[sorted.length-1];
            }
        }
        public double prc( double k ){
            if( k<0 )throw new IllegalArgumentException( "k<0" );
            if( k>1 )throw new IllegalArgumentException( "k>1" );
            if( k==0 )return sorted[0];
            if( k==1 )return sorted[sorted.length-1];
            if( (count%2)==0 ){
                int i1 = (int)(count*k);
                int i2 = i1+1;
                if( i1<sorted.length && i2<sorted.length ){
                    return (sorted[i1] + sorted[i2])/2;
                }else{
                    return sorted[i1];
                }
            }else{
                int i1 = (int)(count*k);
                return sorted[i1];
            }
        }
        public void print(String prefix){
            if( prefix!=null ) System.out.print(prefix);
            System.out.print("cnt="+count);
            if( count>1 ){
                double k = 0.000001;
                System.out.print(" min="+min*k);
                System.out.print(" avg="+avg*k);
                System.out.print(" max="+max*k);

                System.out.print(" 50% "+prc(0.5)*k);
                System.out.print(" 90% "+prc(0.9)*k);
                System.out.print(" 99.7% "+prc(0.997)*k);
            }
            System.out.println();
        }

        public String toString(){
            return toString(null);
        }

        public String toString(EchoFormat fmt){
            if( fmt==null )fmt = defEcho;

            StringBuilder sb = new StringBuilder();
            sb.append(fmt.cnt(count));
            if( count>1 ){
                double k = 0.000001;
                sb.append(fmt.min(min * k));
                sb.append(fmt.avg(avg * k));
                sb.append(fmt.max(max * k));

                sb.append(fmt.p50(prc(0.5) * k));
                sb.append(fmt.p90(prc(0.9) * k));
                sb.append(fmt.p997(prc(0.997) * k));
            }
            return sb.toString();
        }
    }

    public static class EchoFormat {
        protected FullDecFormat cnt = new FullDecFormat("### ###");
        public FullDecFormat cnt(){ return cnt; }
        protected Function<Number,String> f_cnt = n -> "cnt="+cnt().format(n);
        public String cnt(Number n){ return f_cnt.apply(n); }

        protected FullDecFormat min = new FullDecFormat("###.000");
        public FullDecFormat min(){ return min; }
        protected Function<Number,String> f_min = n -> " min="+min().format(n);
        public String min(Number n){ return f_min.apply(n); }

        protected FullDecFormat avg = new FullDecFormat("###.000");
        public FullDecFormat avg(){ return avg; }
        protected Function<Number,String> f_avg = n -> " avg="+avg().format(n);
        public String avg(Number n){ return f_avg.apply(n); }

        protected FullDecFormat max = new FullDecFormat("###.000");
        public FullDecFormat max(){ return max; }
        protected Function<Number,String> f_max = n -> " max="+max().format(n);
        public String max(Number n){ return f_max.apply(n); }

        protected FullDecFormat p50 = new FullDecFormat("###.000");
        public FullDecFormat p50(){ return p50; }
        protected Function<Number,String> f_p50 = n -> " 50%="+p50().format(n);
        public String p50(Number n){ return f_p50.apply(n); }

        protected FullDecFormat p90 = new FullDecFormat("###.000");
        public FullDecFormat p90(){ return p90; }
        protected Function<Number,String> f_p90 = n -> " 90%="+p90().format(n);
        public String p90(Number n){ return f_p90.apply(n); }

        protected FullDecFormat p997 = new FullDecFormat("###.000");
        public FullDecFormat p997(){ return p997; }
        protected Function<Number,String> f_p997 = n -> " 99.7%="+p997().format(n);
        public String p997(Number n){ return f_p997.apply(n); }
    }
    private static EchoFormat defEcho = new EchoFormat();

    public Echo echo(){
        return new Echo();
    }

    public Echo lastEcho;
    public Echo echo( long timeout ){
        if( lastEcho==null ){
            lastEcho = new Echo();
            return lastEcho;
        }

        if( (System.currentTimeMillis()-lastEcho.time)>timeout ){
            lastEcho = new Echo();
            return lastEcho;
        }

        return lastEcho;
    }
    public void echo(long timeout, Consumer<Echo> echoConsumer){
        if( echoConsumer==null )throw new IllegalArgumentException( "echoConsumer==null" );
        if( lastEcho==null ){
            lastEcho = new Echo();
            return;
        }

        if( (System.currentTimeMillis()-lastEcho.time)>timeout ){
            lastEcho = new Echo();
            echoConsumer.accept(lastEcho);
        }
    }
}
