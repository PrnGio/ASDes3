/**
 * 
 */
package com.esercizi.es.asdes3;

// TODO completare gli import se necessario

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

/**
 * Un time slot è un intervallo di tempo continuo che può essere associato ad
 * una prenotazione. Gli oggetti della classe sono immutabili. Non sono ammessi
 * time slot che iniziano e finiscono nello stesso istante.
 * 
 * @author Luca Tesei
 *
 */
public class TimeSlot implements Comparable<TimeSlot> {

    /**
     * Rappresenta la soglia di tolleranza da considerare nella sovrapposizione
     * di due Time Slot. Se si sovrappongono per un numero di minuti minore o
     * uguale a questa soglia allora NON vengono considerati sovrapposti.
     */
    public static final int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 5;

    private final GregorianCalendar start;

    private final GregorianCalendar stop;

    /**
     * Crea un time slot tra due istanti di inizio e fine
     * 
     * @param start
     *                  inizio del time slot
     * @param stop
     *                  fine del time slot
     * @throws NullPointerException
     *                                      se uno dei due istanti, start o
     *                                      stop, è null
     * @throws IllegalArgumentException
     *                                      se start è uguale o successivo a
     *                                      stop
     */
    public TimeSlot(GregorianCalendar start, GregorianCalendar stop) {
        if(start == null)
        {
            throw new NullPointerException("start è null");
        }
        else if(stop == null)
        {
            throw new NullPointerException("stop è null");
        }
        else if(start.after(stop)|| start==stop)
        {
            throw new NullPointerException("start è uguale o successivo a stop");
        }
        this.start = start;
        this.stop = stop;
    }

    /**
     * @return the start
     */
    public GregorianCalendar getStart() {
        return start;
    }

    /**
     * @return the stop
     */
    public GregorianCalendar getStop() {
        return stop;
    }

    /*
     * Un time slot è uguale a un altro se rappresenta esattamente lo stesso
     * intervallo di tempo, cioè se inizia nello stesso istante e termina nello
     * stesso istante.
     */
    @Override
    public boolean equals(Object obj) {
        TimeSlot other = (TimeSlot) obj;
        return this.start.equals(other.start) && this.stop.equals(other.stop);
    }

    /*
     * Il codice hash associato a un timeslot viene calcolato a partire dei due
     * istanti di inizio e fine, in accordo con i campi usati per il metodo
     * equals.
     */
    @Override
    public int hashCode() {
        int result = start.hashCode();
        result = 31 * result + stop.hashCode();
        return result;
    }

    /*
     * Un time slot precede un altro se inizia prima. Se due time slot iniziano
     * nello stesso momento quello che finisce prima precede l'altro. Se hanno
     * stesso inizio e stessa fine sono uguali, in compatibilità con equals.
     */
    @Override
    public int compareTo(TimeSlot o) {
        int startComparison = this.start.compareTo(o.start);
        if (startComparison != 0) {
            return startComparison;
        } else {
            return this.stop.compareTo(o.stop);
        }
    }
    /**
     * Determina il numero di minuti di sovrapposizione tra questo timeslot e
     * quello passato.
     * 
     * @param o
     *              il time slot da confrontare con questo
     * @return il numero di minuti di sovrapposizione tra questo time slot e
     *         quello passato, oppure -1 se non c'è sovrapposizione. Se questo
     *         time slot finisce esattamente al millisecondo dove inizia il time
     *         slot <code>o</code> non c'è sovrapposizione, così come se questo
     *         time slot inizia esattamente al millisecondo in cui finisce il
     *         time slot <code>o</code>. In questi ultimi due casi il risultato
     *         deve essere -1 e non 0. Nel caso in cui la sovrapposizione non è
     *         di un numero esatto di minuti, cioè ci sono secondi e
     *         millisecondi che avanzano, il numero dei minuti di
     *         sovrapposizione da restituire deve essere arrotondato per difetto
     * @throws NullPointerException
     *                                      se il time slot passato è nullo
     * @throws IllegalArgumentException
     *                                      se i minuti di sovrapposizione
     *                                      superano Integer.MAX_VALUE
     */
    public int getMinutesOfOverlappingWith(TimeSlot o) {
            if (o == null) {
                throw new NullPointerException("Il time slot passato è nullo.");
            }

            long startMillis = Math.max(this.start.getTimeInMillis(), o.start.getTimeInMillis());
            long endMillis = Math.min(this.stop.getTimeInMillis(), o.stop.getTimeInMillis());

            if (startMillis >= endMillis) {
                return -1; // Nessuna sovrapposizione
            }

            long overlapMillis = endMillis - startMillis;
            long overlapMinutes = overlapMillis / (60 * 1000); // Conversione in minuti

            if (overlapMinutes > Integer.MAX_VALUE) {
                throw new IllegalArgumentException("I minuti di sovrapposizione superano Integer.MAX_VALUE.");
            }

            return (int) overlapMinutes;
        }

    /**
     * Determina se questo time slot si sovrappone a un altro time slot dato,
     * considerando la soglia di tolleranza.
     * 
     * @param o
     *              il time slot che viene passato per il controllo di
     *              sovrapposizione
     * @return true se questo time slot si sovrappone per più (strettamente) di
     *         MINUTES_OF_TOLERANCE_FOR_OVERLAPPING minuti a quello passato
     * @throws NullPointerException
     *                                  se il time slot passato è nullo
     */
    public boolean overlapsWith(TimeSlot o) {
        if (o == null) {
            throw new NullPointerException("Il time slot passato è nullo.");
        }

        int MINUTES_OF_TOLERANCE_FOR_OVERLAPPING = 1; // Definisci questa costante in base al tuo requisito

        long startMillis = Math.max(this.start.getTimeInMillis(), o.start.getTimeInMillis());
        long endMillis = Math.min(this.stop.getTimeInMillis(), o.stop.getTimeInMillis());

        if (startMillis >= endMillis) {
            return false; // Nessuna sovrapposizione
        }

        long overlapMillis = endMillis - startMillis;
        long overlapMinutes = overlapMillis / (60 * 1000); // Conversione in minuti

        return overlapMinutes > MINUTES_OF_TOLERANCE_FOR_OVERLAPPING;
    }


    /*
     * Ridefinisce il modo in cui viene reso un TimeSlot con una String.
     * 
     * Esempio 1, stringa da restituire: "[4/11/2019 11.0 - 4/11/2019 13.0]"
     * 
     * Esempio 2, stringa da restituire: "[10/11/2019 11.15 - 10/11/2019 23.45]"
     * 
     * I secondi e i millisecondi eventuali non vengono scritti.
     */
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("d/M/yyyy H.m");
        return "[" + sdf.format(start.getTime()) + " - " + sdf.format(stop.getTime()) + "]";
    }


}
