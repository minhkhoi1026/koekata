package com.example.koekata.ui.main.Statistic;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.koekata.models.UserEvent;
import com.example.koekata.models.UserRepository;
import com.example.koekata.models.UserTask;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class StatisticViewModel extends ViewModel {
    private static long millisecPerDay = 86400000;
    private static long millisecPerWeek = 86400000 * 7;

    private UserRepository repository;

    public MediatorLiveData<Integer> getCurrentMonth() {
        return currentMonth;
    }

    public MediatorLiveData<Integer> getCurrentYear() {
        return currentYear;
    }

    private MediatorLiveData<Integer> currentMonth;
    private MediatorLiveData<Integer> currentYear;
    private MediatorLiveData<String> monthYear;
    private MediatorLiveData<Integer> status;
    private MediatorLiveData<Long> currentTime;
    public MediatorLiveData<Long> getCurrentTime() {
        return currentTime;
    }

//pomodoro
    private MediatorLiveData<Integer> totalPomodoroCompletedPerDay;
    private MediatorLiveData<Integer> totalPomodoroCompletedPerWeek;
    private MediatorLiveData<Integer> totalPomodoroCompletedPerMonth;
    private MediatorLiveData<Integer> totalPomodoroCompletedPerYear;
    private MediatorLiveData<Integer> totalPomodoroCompleted;
    private MediatorLiveData<ArrayList<Integer>> pomodoroStatistic;

    //schedule


    

    private MediatorLiveData<ArrayList<Integer>> totalScheduleCompleted;
    private MediatorLiveData<ArrayList<Integer>> schedulesStatistic;

    public MediatorLiveData<ArrayList<Integer>> getTotalScheduleCompleted() {
        return totalScheduleCompleted;
    }

    public MediatorLiveData<ArrayList<Integer>> getSchedulesStatistic() {
        return schedulesStatistic;
    }

    public MediatorLiveData<ArrayList<Double>> getTotalDailyTasksCompleted() {
        return totalDailyTasksCompleted;
    }

    public MediatorLiveData<ArrayList<Double>> getDailyTasksStatistic() {
        return dailyTasksStatistic;
    }

    //daily-task
    private MediatorLiveData<ArrayList<Integer>> dailyTaskNumStatistic;
    private MediatorLiveData<ArrayList<Integer>> dailyTaskDenStatistic;
    private MediatorLiveData<ArrayList<Integer>> totalDailyTaskNum;
    private MediatorLiveData<ArrayList<Integer>> totalDailyTaskDen;

    public MediatorLiveData<ArrayList<Integer>> getTotalDailyTaskNum() {
        return totalDailyTaskNum;
    }

    public MediatorLiveData<ArrayList<Integer>> getTotalDailyTaskDen() {
        return totalDailyTaskDen;
    }

    public MediatorLiveData<ArrayList<Integer>> getDailyTaskNum() {
        return dailyTaskNumStatistic;
    }

    public MediatorLiveData<ArrayList<Integer>> getDailyTaskDen() {
        return dailyTaskDenStatistic;
    }

    private MediatorLiveData<ArrayList<Double>> totalDailyTasksCompleted;
    private MediatorLiveData<ArrayList<Double>> dailyTasksStatistic;

    @Inject
    public StatisticViewModel(UserRepository repo){
        repository = repo;
        currentTime = new MediatorLiveData<>();
        currentTime.setValue(System.currentTimeMillis());
        pomodoroStatistic = new MediatorLiveData<ArrayList<Integer>>();
        totalPomodoroCompletedPerDay = new MediatorLiveData<Integer>();
        totalPomodoroCompletedPerWeek = new MediatorLiveData<Integer>();
        totalPomodoroCompletedPerMonth = new MediatorLiveData<Integer>();
        totalPomodoroCompletedPerYear = new MediatorLiveData<Integer>();
        totalPomodoroCompleted = new MediatorLiveData<Integer>();
        currentMonth = new MediatorLiveData<Integer>();
        currentYear = new MediatorLiveData<Integer>();
        monthYear = new MediatorLiveData<String>();

        totalScheduleCompleted = new MediatorLiveData<>();
        schedulesStatistic = new MediatorLiveData<>();
        totalDailyTasksCompleted = new MediatorLiveData<>();
        dailyTasksStatistic = new MediatorLiveData<>();
        dailyTaskNumStatistic = new MediatorLiveData<>();
        dailyTaskDenStatistic = new MediatorLiveData<>();
        totalDailyTaskNum = new MediatorLiveData<>();
        totalDailyTaskDen = new MediatorLiveData<>();



        totalPomodoroCompletedPerDay.setValue(0);
        totalPomodoroCompletedPerWeek.setValue(0);
        totalPomodoroCompletedPerMonth.setValue(0);
        totalPomodoroCompletedPerYear.setValue(0);
        totalPomodoroCompleted.setValue(0);


        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long current = getCurrentTime().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);
        String formattedDate = formatter.format(calendar.getTime());
        currentMonth.setValue(Integer.parseInt(formattedDate.substring(3,5)));
        currentYear.setValue(Integer.parseInt(formattedDate.substring(6,10)));

        monthYear.setValue(formattedDate.substring(3,10));

    }

    public MediatorLiveData<Integer> getStatus() {
        return status;
    }

    public MediatorLiveData<Integer> getTotalPomodoroCompletedPerDay() {
        return totalPomodoroCompletedPerDay;
    }

    public MediatorLiveData<Integer> getTotalPomodoroCompletedPerWeek() {
        return totalPomodoroCompletedPerWeek;
    }

    public MediatorLiveData<Integer> getTotalPomodoroCompletedPerMonth() {
        return totalPomodoroCompletedPerMonth;
    }

    public MediatorLiveData<Integer> getTotalPomodoroCompletedPerYear() {
        return totalPomodoroCompletedPerYear;
    }

    public MediatorLiveData<Integer> getTotalPomodoroCompleted() {
        return totalPomodoroCompleted;
    }

    public MediatorLiveData<ArrayList<Integer>> getPomodoroStatistic() {
        return pomodoroStatistic;
    }

    public MediatorLiveData<String> getMonthYear() {

        //monthYear.setValue(String.valueOf(getCurrentMonth().getValue()) + "/" + String.valueOf(getCurrentYear().getValue()));
        return monthYear;
    }



    void countTotalPomodoroCompleted(){
        totalPomodoroCompleted.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                totalPomodoroCompleted.postValue(stringLongHashMap.size());
            }
        });
    }

    void countTotalPomodoroCompletedPerDay(){
        totalPomodoroCompletedPerDay.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                long beginCurrentDay = currentTime.getValue() - currentTime.getValue() % millisecPerDay;
                long endCurrentDay = beginCurrentDay + millisecPerDay - 1;
                int count = 0;
                for (Long t : stringLongHashMap.values()) {
                    if (t<= endCurrentDay && t>=beginCurrentDay){
                        count++;
                    }
                }
                totalPomodoroCompletedPerDay.postValue(count);
            }
        });
    }


    void countTotalPomodoroCompletedPerWeek(){
        totalPomodoroCompletedPerWeek.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                long beginCurrentWeek = currentTime.getValue() - currentTime.getValue() % millisecPerWeek;
                long endCurrentWeek = beginCurrentWeek + millisecPerDay - 1;
                int count = 0;
                for (Long t : stringLongHashMap.values()) {
                    if (t<= endCurrentWeek && t>=beginCurrentWeek){
                        count++;
                    }
                }
                totalPomodoroCompletedPerWeek.postValue(count);
            }
        });
    }

    void countTotalPomodoroCompletedPerMonth(){
        totalPomodoroCompletedPerMonth.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                long beginCurrentMonth = getBeginCurrentMonthMilisec();
                long endCurrentMonth = getEndCurrentMonthMilisec();
                int count = 0;
                for (Long t : stringLongHashMap.values()) {
                    if (t<= beginCurrentMonth && t>=endCurrentMonth){
                        count++;
                    }
                }
                totalPomodoroCompletedPerMonth.postValue(count);
            }
        });
    }



    void countTotalPomodoroCompletedPerYear(){
        totalPomodoroCompletedPerYear.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                long beginCurrentYear = getBeginCurrentYearMilisec();
                long endCurrentYear = getEndCurrentYearMilisec();
                int count = 0;
                for (Long t : stringLongHashMap.values()) {
                    if (t<= beginCurrentYear && t>=endCurrentYear){
                        count++;
                    }
                }
                totalPomodoroCompletedPerYear.postValue(count);
            }
        });
    }

    public void countTotalScheduleCompleted(){
        totalScheduleCompleted.addSource(repository.getEventsLiveData(), new Observer<HashMap<String, UserEvent>>() {
            @Override
            public void onChanged(HashMap<String, UserEvent> stringUserEventHashMap) {
                long beginCurrentDay = currentTime.getValue() - currentTime.getValue() % millisecPerDay;
                long endCurrentDay = beginCurrentDay + millisecPerDay - 1;
                long beginCurrentYear = getBeginCurrentYearMilisec();
                long endCurrentYear = getEndCurrentYearMilisec();
                long beginCurrentMonth = getBeginCurrentMonthMilisec();
                long endCurrentMonth = getEndCurrentMonthMilisec();
                long beginCurrentWeek = currentTime.getValue() - currentTime.getValue() % millisecPerWeek;
                long endCurrentWeek = beginCurrentWeek + millisecPerDay - 1;
                ArrayList<Integer> temp = new ArrayList<>();
                int countDay = 0, countWeek = 0, countMonth = 0, countYear = 0, total = 0;
                for (UserEvent userEvent: stringUserEventHashMap.values())
                {
                    if (userEvent.date >=beginCurrentDay &&userEvent.date<=endCurrentDay)
                        countDay++;
                    if (userEvent.date >=beginCurrentWeek &&userEvent.date<=endCurrentWeek)
                        countWeek++;
                    if (userEvent.date >=beginCurrentMonth &&userEvent.date<=endCurrentMonth)
                        countMonth++;
                    if (userEvent.date >=beginCurrentYear &&userEvent.date<=endCurrentYear)
                        countYear++;

                }
                total = stringUserEventHashMap.size();
                temp.add(countDay);
                temp.add(countWeek);
                temp.add(countMonth);
                temp.add(countYear);
                temp.add(total);
                totalScheduleCompleted.postValue(temp);



            }
        });
    }

    public void getTotalDailyTaskListNum(){
        totalDailyTaskNum.addSource(repository.getTaskHistoryLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                ArrayList<Integer> num = new ArrayList<>();
                long beginCurrentDay = currentTime.getValue() - currentTime.getValue() % millisecPerDay;
                long endCurrentDay = beginCurrentDay + millisecPerDay - 1;
                long beginCurrentYear = getBeginCurrentYearMilisec();
                long endCurrentYear = getEndCurrentYearMilisec();
                long beginCurrentMonth = getBeginCurrentMonthMilisec();
                long endCurrentMonth = getEndCurrentMonthMilisec();
                long beginCurrentWeek = currentTime.getValue() - currentTime.getValue() % millisecPerWeek;
                long endCurrentWeek = beginCurrentWeek + millisecPerDay - 1;

                int countDay = 0, countWeek = 0, countMonth = 0, countYear = 0, total = 0;
                for (String i : stringLongHashMap.keySet()) {
                    if (Integer.parseInt(i) >=beginCurrentDay &&Integer.parseInt(i)<=endCurrentDay)
                        countDay+=stringLongHashMap.get(i);
                    if (Integer.parseInt(i) >=beginCurrentWeek &&Integer.parseInt(i)<=endCurrentWeek)
                        countWeek+=stringLongHashMap.get(i);
                    if (Integer.parseInt(i) >=beginCurrentMonth &&Integer.parseInt(i)<=endCurrentMonth)
                        countMonth+=stringLongHashMap.get(i);
                    if (Integer.parseInt(i) >=beginCurrentYear &&Integer.parseInt(i)<=endCurrentYear)
                        countYear+=stringLongHashMap.get(i);
                    total+=stringLongHashMap.get(i);
                }

                num.add(countDay);
                num.add(countWeek);
                num.add(countMonth);
                num.add(countYear);
                num.add(total);

                totalDailyTaskNum.postValue(num);
            }
        });
    }

    public void getTotalDailyTaskListDen(){
        totalDailyTaskDen.addSource(repository.getTasksLiveData(), new Observer<HashMap<String, UserTask>>() {
            @Override
            public void onChanged(HashMap<String, UserTask> stringUserTaskHashMap) {
                ArrayList<Integer> den = new ArrayList<>();
                long beginCurrentDay = currentTime.getValue() - currentTime.getValue() % millisecPerDay;
                long endCurrentDay = beginCurrentDay + millisecPerDay - 1;
                long beginCurrentYear = getBeginCurrentYearMilisec();
                long endCurrentYear = getEndCurrentYearMilisec();
                long beginCurrentMonth = getBeginCurrentMonthMilisec();
                long endCurrentMonth = getEndCurrentMonthMilisec();
                long beginCurrentWeek = currentTime.getValue() - currentTime.getValue() % millisecPerWeek;
                long endCurrentWeek = beginCurrentWeek + millisecPerDay - 1;



                int countDay = 0, countWeek = 0, countMonth = 0, countYear = 0, total = 0;
                Long minn = null;

                for (UserTask userTask: stringUserTaskHashMap.values()) {
                    if (minn == null)
                        minn = userTask.start;
                    else{
                        if (userTask.start<minn)
                            minn = userTask.start;

                    }

                }

                for (UserTask userTask: stringUserTaskHashMap.values()) {

                    countDay+=countTask(userTask.start, userTask.repeat, beginCurrentDay,endCurrentDay);
                    countDay+=countTask(userTask.start, userTask.repeat, beginCurrentDay,endCurrentDay);
                    countDay+=countTask(userTask.start, userTask.repeat, beginCurrentDay,endCurrentDay);
                    countDay+=countTask(userTask.start, userTask.repeat, beginCurrentDay,endCurrentDay);
                    total+=countTask(userTask.start, userTask.repeat, minn,currentTime.getValue());
                }

                den.add(countDay);
                den.add(countWeek);
                den.add(countMonth);
                den.add(countYear);
                den.add(total);

                totalDailyTaskDen.postValue(den);
            }
        });
    }

    private int countTask(Long start, Long repeat, long begin, long end) {
        int ans;
        if (repeat==0)
        {
            if ((start>=begin)&&(start<=end))
                ans = 1;
            else
                ans = 0;
        }
        else
        {
            int s = (int)((begin - start)/repeat);
            int e = (int)((end - start)/repeat);
            ans= e-s+1;

        }
        return ans;
    }

    public void getTotalDailyTaskListCompleted(){
        ArrayList<Double> temp = new ArrayList<>();
        ArrayList<Integer> num = totalDailyTaskNum.getValue();
        ArrayList<Integer> den = totalDailyTaskDen.getValue();
        int s = num.size();
        for(int i=0;i<s;++i){
            temp.add(1.0*num.get(i)/den.get(i));
        }
        totalDailyTasksCompleted.postValue(temp);
    }

    public void initScheduleStatistic(){
        schedulesStatistic.removeSource(repository.getEventsLiveData());
        schedulesStatistic.addSource(repository.getEventsLiveData(), new Observer<HashMap<String, UserEvent>>() {
            @Override
            public void onChanged(HashMap<String, UserEvent> stringUserEventHashMap) {
                ArrayList<Integer> scheduleData = new ArrayList<Integer>();
                int day = Integer.parseInt(getDay(currentMonth.getValue(), currentYear.getValue()));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String b = "01/" + String.valueOf(currentMonth.getValue()) + "/"
                        + String.valueOf(currentYear.getValue()) + " 00:00:00";
                Date date = null;
                try {
                    date = formatter.parse(b);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                for (int i=0;i<day;++i){
                    int count = 0;
                    long beginDay = millis + i*millisecPerDay;
                    long endDay = millis + (i+1)*millisecPerDay - 1;


                    for (UserEvent t : stringUserEventHashMap.values()){
                        if ((t.date>=beginDay)&&(t.date<=endDay))
                            ++count;
                    }
                    scheduleData.add(count);

                }
                schedulesStatistic.setValue(scheduleData);

            }
        });
    }



    public void initDailyTaskNum(){
        dailyTaskNumStatistic.removeSource(repository.getTaskHistoryLiveData());
        dailyTaskNumStatistic.addSource(repository.getTaskHistoryLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {
                ArrayList<Integer> dailyTaskListNum = new ArrayList<Integer>();
                int day = Integer.parseInt(getDay(currentMonth.getValue(), currentYear.getValue()));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String b = "01/" + String.valueOf(currentMonth.getValue()) + "/"
                        + String.valueOf(currentYear.getValue()) + " 00:00:00";
                Date date = null;
                try {
                    date = formatter.parse(b);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                for (int i=0;i<day;++i){
                    int count = 0;
                    long beginDay = millis + i*millisecPerDay;
                    long endDay = millis + (i+1)*millisecPerDay - 1;


                    for (String j : stringLongHashMap.keySet()) {
                        if (Integer.parseInt(j) >=beginDay &&Integer.parseInt(j)<=endDay)
                            count+=stringLongHashMap.get(i);
                    }

        //viet lai
//                    for (UserEvent t : dailyTaskListNum.values()){
//                        if ((t.date>=beginDay)&&(t.date<=endDay))
//                            ++count;
//                    }
                    dailyTaskListNum.add(count);

                }
                dailyTaskNumStatistic.setValue(dailyTaskListNum);

            }
        });
    }


    public void initDailyTaskDen(){
        dailyTaskDenStatistic.removeSource(repository.getTasksLiveData());
        dailyTaskDenStatistic.addSource(repository.getTasksLiveData(), new Observer<HashMap<String, UserTask>>() {
            @Override
            public void onChanged(HashMap<String, UserTask> stringUserTaskHashMap) {
                ArrayList<Integer> dailyTaskListDen = new ArrayList<Integer>();
                int day = Integer.parseInt(getDay(currentMonth.getValue(), currentYear.getValue()));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String b = "01/" + String.valueOf(currentMonth.getValue()) + "/"
                        + String.valueOf(currentYear.getValue()) + " 00:00:00";
                Date date = null;
                try {
                    date = formatter.parse(b);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                for (int i=0;i<day;++i){
                    int count = 0;
                    long beginDay = millis + i*millisecPerDay;
                    long endDay = millis + (i+1)*millisecPerDay - 1;

                    for(UserTask userTask: stringUserTaskHashMap.values())
                    {
                        count = countTask(userTask.start,userTask.repeat,beginDay,endDay);
                    }
                    //viet lai
//                    for (UserEvent t : dailyTaskListNum.values()){
//                        if ((t.date>=beginDay)&&(t.date<=endDay))
//                            ++count;
//                    }
                    dailyTaskListDen.add(count);

                }
                dailyTaskDenStatistic.setValue(dailyTaskListDen);

            }
        });
    }

    public void initDailyTaskListStatistic(){
        ArrayList<Double> temp = new ArrayList<>();
        ArrayList<Integer> num = dailyTaskNumStatistic.getValue();
        ArrayList<Integer> den = dailyTaskDenStatistic.getValue();
        int s = num.size();
        for (int i=0;i<s;++i){
            temp.add(1.0*num.get(i)/den.get(i));
        }
        totalDailyTasksCompleted.postValue(temp);
    }
    private long getEndCurrentMonthMilisec() {

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long current = getCurrentTime().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);

        String formattedDate = formatter.format(calendar.getTime());
        int month = Integer.parseInt(formattedDate.substring(3,5));
        int year = Integer.parseInt(formattedDate.substring(6,10));
        String dayInMonth = getDay(month,year);
        String b = dayInMonth + "/" + formattedDate.substring(3,11)  + "23:59:59";
        Date date = null;
        try {
            date = formatter.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    private String getDay(int month, int year) {
        int day;
        if ((month == 3)||(month == 5)||(month == 7)||(month == 8)||(month == 10)||(month == 12)||(month == 1)){
            day = 31;
        }
        else if ((month == 4)||(month == 6)||(month == 9)||(month == 11)){
            day = 30;
        }
        else{
            if (isLeap(year)){
                day = 29;
            }
            else
                day = 28;
        }
        return String.valueOf(day);
    }

    private boolean isLeap(int year) {
        if ((year % 400 == 0) || ((year %4 == 0) && (year % 100 !=0)))
            return true;
        return false;
    }

    private long getBeginCurrentYearMilisec() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long current = getCurrentTime().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);
        String formattedDate = formatter.format(calendar.getTime());
        String b = "01/01" + formattedDate.substring(5,11) + "00:00:00";
        //System.out.println("After formatting: " + b);
        Date date = null;
        try {
            date = formatter.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    private long getEndCurrentYearMilisec() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long current = getCurrentTime().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);
        String formattedDate = formatter.format(calendar.getTime());
        String b = "31/12" + formattedDate.substring(5,11) + "23:59:59";
        Date date = null;
        try {
            date = formatter.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    private long getBeginCurrentMonthMilisec() {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        long current = 	getCurrentTime().getValue();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(current);
        String formattedDate = formatter.format(calendar.getTime());
        String b = "01" + formattedDate.substring(2,11) + "00:00:00";
        //System.out.println("After formatting: " + b);
        Date date = null;
        try {
            date = formatter.parse(b);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis;
    }

    void initPomodoroStatistic(){
        pomodoroStatistic.removeSource(repository.getPomodorosLiveData());
        pomodoroStatistic.addSource(repository.getPomodorosLiveData(), new Observer<HashMap<String, Long>>() {
            @Override
            public void onChanged(HashMap<String, Long> stringLongHashMap) {

                ArrayList<Integer> pomodoroData = new ArrayList<Integer>();
                int day = Integer.parseInt(getDay(currentMonth.getValue(), currentYear.getValue()));
                DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String b = "01/" + String.valueOf(currentMonth.getValue()) + "/"
                        + String.valueOf(currentYear.getValue()) + " 00:00:00";
                Date date = null;
                try {
                    date = formatter.parse(b);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long millis = date.getTime();
                for (int i=0;i<day;++i){
                    int count = 0;
                    long beginDay = millis + i*millisecPerDay;
                    long endDay = millis + (i+1)*millisecPerDay - 1;


                    for (Long t : stringLongHashMap.values()){
                        if ((t>=beginDay)&&(t<=endDay))
                            ++count;
                    }
                    pomodoroData.add(count);

                }
                pomodoroStatistic.setValue(pomodoroData);
            }
        });





    }

    void changeForward(){

        if (currentMonth.getValue() ==12){
            currentMonth.postValue(1);
            currentYear.postValue(currentYear.getValue()+1);
        }
        else
            currentMonth.postValue(currentMonth.getValue()+1);
        monthYear.postValue(String.valueOf(getCurrentMonth().getValue()) + "/" + String.valueOf(getCurrentYear().getValue()));
        //initStatistic();
    }

    void changeBack(){

        if (currentMonth.getValue() ==1){
            currentMonth.postValue(12);
            currentYear.postValue(currentYear.getValue()-1);
        }
        else
            currentMonth.postValue(currentMonth.getValue()-1);
        monthYear.postValue(String.valueOf(getCurrentMonth().getValue()) + "/" + String.valueOf(getCurrentYear().getValue()));
        //initStatistic();

    }


}
