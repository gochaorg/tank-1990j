package xyz.cofe.game.tank;

import java.util.Arrays;
import java.util.function.Consumer;

public class TCounter {
    public final int length;
    public final long times[];
    public TCounter(int len){
        if( len<1 )throw new IllegalArgumentException( "len<1" );
        length = len;
        times = new long[len];
        pointer = 0;
    }
    private int pointer;
    public long started;
    public long stopped;
    public void start(){
        started = System.nanoTime();
    }
    public void stop(){
        stopped = System.nanoTime();
        times[pointer%length] = stopped - started;
        pointer++;
    }

    public class Echo {
        public int count;
        public double summa;
        public double min;
        public double max;
        public double avg;
        public long[] sorted;
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
