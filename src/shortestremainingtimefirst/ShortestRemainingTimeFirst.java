package shortestremainingtimefirst;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

class Process {

    int id;
    int at;
    int bt;
    int wt;
    int ttt;
    int rt;
    int ct;

    public Process(int pid, int arrivalTime, int burstTime) {
        this.id = pid;
        this.at = arrivalTime;
        this.bt = burstTime;
        this.wt = 0;
        this.ttt = 0;
        this.rt = burstTime;
        this.ct = 0;
    }

    public int getId() {
        return id;
    }

    public int getArrivalTime() {
        return at;
    }

    public int getRemainingTime() {
        return rt;
    }
}

public class ShortestRemainingTimeFirst {

    public static void main(String[] args) {       

        Scanner sc = new Scanner(System.in);

        System.out.print("\nNumber of processes (3 - 6): ");
        int n = sc.nextInt();

        while (n < 3 || n > 6) {
            System.out.println("\nInvalid!");
            
            System.out.print("\nEnter the number of processes (3 - 6): ");
            n = sc.nextInt();
        }

        ArrayList<Process> ps = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.println("Process #" + (i + 1) + " :");
            System.out.print("AT: ");
            int at = sc.nextInt();
            System.out.print("BT: ");
            int bt = sc.nextInt();
            Process p = new Process((i + 1), at, bt);
            ps.add(p);
        }

        Collections.sort(ps, (Process p1, Process p2) -> Integer.compare(p1.at, p2.at));

        ArrayList<Process> rq = new ArrayList<>();

        int crt = 0, st = 0, et = 0;
        double ttt = 0;
        double twt = 0;
        double tbt = 0;

        int pc = 0;

        StringBuilder tl = new StringBuilder();
        StringBuilder pg = new StringBuilder();
        StringBuilder bt = new StringBuilder();
        StringBuilder tg = new StringBuilder();

        Process cp = null;

        while (pc != n) {
            int nat = Integer.MAX_VALUE;

            for (Process p : ps) {
                if (p.at == crt) {
                    rq.add(p);
                } else if (p.at > crt) {
                    nat = Math.min(nat, p.at);
                }
            }

            if (rq.size() > 1) {
                rq.sort(Comparator.comparing(Process::getRemainingTime)
                        .thenComparing(Process::getArrivalTime).thenComparing(Process::getId));
            }

            if (!rq.isEmpty()) {
                cp = rq.get(0);

                int te = nat - crt;
                if (cp.rt <= te) {
                    st = crt;

                    et = crt + cp.rt;

                    crt = et;

                    ps.get(ps.indexOf(cp)).rt = 0;

                    ps.get(ps.indexOf(cp)).ct = crt;
                    ps.get(ps.indexOf(cp)).ttt = cp.ct - cp.at;
                    ps.get(ps.indexOf(cp)).wt = cp.ttt - cp.bt;

                    rq.remove(cp);

                    pc++;
                } else {
                    st = crt;

                    et = crt + te;

                    crt = et;

                    ps.get(ps.indexOf(cp)).rt -= te;
                }

                tl.append("-=-=-=-=-");

                pg.append(" |   ").append("P").append(ps.get(ps.indexOf(cp)).id).append("   ");
                if (pc != n) {
                    tg.append("     ").append(st);
                } else {
                    tl.append("-=-=-=-=-");
                    pg.append("|");
                    tg.append("      ").append(st).append("       ").append(et);
                    bt.append("-=-=-=-=-");
                }

                if (st >= 10) {
                    tg.append("   ");
                } else {
                    tg.append("    ");
                }

                bt.append("-=-=-=-=-");
            } else {
                tl.append("-=-=-=-=-");

                pg.append(" |   ").append("--").append("   ");
                tg.append("     ").append(crt);

                if (crt >= 10) {
                    tg.append("  ");
                } else {
                    tg.append("    ");
                }

                bt.append("-=-=-=-=-");

                crt = nat;
            }
        }

        System.out.println("\nGantt Chart:");
        System.out.println(" " + tl.toString());
        System.out.println(pg.toString());
        System.out.println(" " + bt.toString());
        System.out.println(tg.toString());

        Collections.sort(ps, new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2) {
                return Integer.compare(p1.id, p2.id);
            }
        });

        System.out.println("\nPID     \tAT          \tBT        \tCT             \tTAT            \tWT          ");
        for (Process p : ps) {
            System.out.println(p.id + "\t\t" + p.at + "\t\t" + p.bt + "\t\t" + p.ct + "\t\t" + p.ttt + "\t\t" + p.wt);
            ttt += p.ttt;
            twt += p.wt;
            tbt += p.bt;
        }

        double avgtt = ttt / n;
        double avgwt = twt / n;
        double cpu = tbt / et * 100;

        DecimalFormat df = new DecimalFormat("#.##");
        System.out.println("AVG TAT: " +  df.format(avgtt) + " ms");
        System.out.println("AVG WAT: " + df.format(avgwt) + " ms");
                     
        System.out.println("CPU Util: "  + df.format(cpu) + "%");
    }

}
