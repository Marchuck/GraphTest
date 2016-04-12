package select;

import com.sun.javafx.beans.annotations.NonNull;

/**
 * @author Lukasz Marczak
 * @since 11.04.16.
 */
public class Selector {

    public void insert(@NonNull AgdsRecord current, @NonNull AgdsRecord newRecord) {
        insert(current, newRecord, 0);
    }

    public void insert(@NonNull AgdsRecord current, @NonNull AgdsRecord newRecord, int tableColumnIndex) {
        for (AgdsRecord record : current.next) {
            String thisRecordProperty = record.getColumn(tableColumnIndex);
            String newRecordProperty = newRecord.getColumn(tableColumnIndex);
            if (thisRecordProperty.equals(newRecordProperty)) {
                current = record;
                ++tableColumnIndex;
                insert(current, newRecord, tableColumnIndex);
                return;
            }
        }
        current.next.add(newRecord);
    }

    public void printlnSelf(AgdsRecord root){
        System.out.println("Agds record: "+root);
    }

    public static void main(String[] args) {
        Selector selector = new Selector();

        AgdsRecord root = new AgdsRecord();
        AgdsRecord newRecord1 = new AgdsRecord("Krk", "Czarnowiejska", "Jan", "Maj", "EAIiIB", "122");
        AgdsRecord newRecord2 = new AgdsRecord("Krk", "Czarnowiejska", "Adam", "Maj", "EAIiIB", "312");
        AgdsRecord newRecord3 = new AgdsRecord("Wawa", "Czarnowiejska", "Ola", "Maj", "EAIiIB", "12");
        AgdsRecord newRecord4 = new AgdsRecord("Krk", "Budryka", "Jan", "Maj", "EAIiIB", "112");

        selector.insert(root, newRecord1, 0);
        selector.insert(root, newRecord2, 0);
        selector.insert(root, newRecord3, 0);
        selector.insert(root, newRecord4, 0);
    }
}
