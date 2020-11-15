package com.infinitydheer.themanager.presentation.view.fragment.task;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.infinitydheer.themanager.R;
import com.infinitydheer.themanager.data.executor.WorkExecutor;
import com.infinitydheer.themanager.data.repository.TaskDataRepository;
import com.infinitydheer.themanager.data.repository.datasource.task.TaskDataStoreFactory;
import com.infinitydheer.themanager.domain.interactor.task.UCEditTask;
import com.infinitydheer.themanager.domain.utils.CalendarUtils;
import com.infinitydheer.themanager.presentation.UIThread;
import com.infinitydheer.themanager.presentation.model.TaskModel;
import com.infinitydheer.themanager.presentation.model.mapper.TaskModelDataMapper;
import com.infinitydheer.themanager.presentation.presenter.task.EditTaskPresenter;
import com.infinitydheer.themanager.presentation.view.fragment.BaseFragment;
import com.infinitydheer.themanager.presentation.view.iview.task.EditTaskView;

import java.util.Calendar;

public class EditTaskFragment extends BaseFragment implements EditTaskView {
    private final static String PARAM_ID="param_id";
    private final static String PARAM_QUAD="param_quad";

    private DatePickerDialog mDatePickerDialog;
    private EditTaskPresenter mPresenter;
    private EditText mTaskNameBox, mTaskNoteBox, mTaskDueBox;
    private TextView mTaskNameErrorLabel;

    private TaskModel mModelShown;

    public static EditTaskFragment forTask(long id, int quad){
        final Bundle instanceState = new Bundle();
        instanceState.putLong(PARAM_ID, id);
        instanceState.putInt(PARAM_QUAD, quad);
        final EditTaskFragment fragment=new EditTaskFragment();
        fragment.setArguments(instanceState);
        return fragment;
    }

    public EditTaskFragment(){setRetainInstance(true);}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.view_edit_task, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialiseV();
        initialiseD();

        this.mPresenter.setView(this);
        this.mPresenter.initialise(getTaskID());
    }

    @Override
    public void renderTask(TaskModel model) {
        this.mModelShown =model;

        this.mTaskNameBox.setText(model.getName());
        this.mTaskNoteBox.setText(model.getNote());
        this.mTaskDueBox.setText(CalendarUtils.convertToString(model.getDate(),false));
    }

    private void initialiseD(){
        TaskDataStoreFactory factory=new TaskDataStoreFactory(getActivity());
        WorkExecutor executor=WorkExecutor.getInstance();
        TaskDataRepository repository=new TaskDataRepository(factory,executor);
        UIThread uiThread=UIThread.getInstance();
        UCEditTask ucEditTask=new UCEditTask(repository,uiThread,executor);
        this.mPresenter =new EditTaskPresenter(ucEditTask, new TaskModelDataMapper());
        this.mModelShown =new TaskModel();
    }

    private void initialiseV(){
        Activity activity=getActivity();
        if(activity==null) return;

        this.mTaskNameBox =activity.findViewById(R.id.et_task_name);
        this.mTaskDueBox =activity.findViewById(R.id.et_task_due);
        this.mTaskNoteBox =activity.findViewById(R.id.et_task_note);
        this.mTaskNameErrorLabel =activity.findViewById(R.id.tv_no_task_name_error);

        setV();
    }

    private void setV(){
        if(this.mTaskDueBox !=null){
            this.mTaskDueBox.setFocusable(false);
            this.mTaskDueBox.setLongClickable(false);
            this.mTaskDueBox.setOnClickListener(view-> showDatePickerDialog());
        }

        this.mTaskNameBox.requestFocus();

        hideNoTaskNameError();
    }

    public void saveTask(boolean isNewTask){
        this.mModelShown.setName(mTaskNameBox.getText().toString());
        this.mModelShown.setNote(mTaskNoteBox.getText().toString());
        this.mModelShown.setDate(CalendarUtils.convertToCalendar(mTaskDueBox.getText().toString(),false));
        this.mModelShown.setQuad(getTaskQuad());

        if(this.mModelShown.isDone()==-1) this.mModelShown.setDone(0);

        this.mPresenter.saveTask(mModelShown, isNewTask);
    }

    private void showDatePickerDialog(){
        Activity activity=getActivity();
        if(activity==null) return;

        Calendar calendar=Calendar.getInstance();
        int year=calendar.get(Calendar.YEAR);int date=calendar.get(Calendar.DATE);
        int month=calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(activity,dateSetListener,
                year,month,date);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }

    private void setTaskDueContent(Calendar c){
        String s= CalendarUtils.convertToString(c,false);
        this.mTaskDueBox.setText(s);
    }

    DatePickerDialog.OnDateSetListener dateSetListener= (datePicker, year, month, date) -> {
        Calendar selectedDate=Calendar.getInstance();
        selectedDate.set(year,month,date);
        EditTaskFragment.this.setTaskDueContent(selectedDate);
    };

    /**
     *********************All the utility functions*********************
     */

    private long getTaskID(){
        final Bundle args=this.getArguments();
        if(args==null) return -1;
        return args.getLong(PARAM_ID, -1);
    }
    private int getTaskQuad(){
        final Bundle args=this.getArguments();
        if(args==null) return 1;
        return args.getInt(PARAM_QUAD, 1);
    }

    @Override
    public void onNoNameOnSave() {
        showNoTaskNameError();
    }
    @Override
    public void hideProgress() {

    }
    @Override
    public void showProgress() {

    }
    @Override
    public void hideRetry() {

    }
    @Override
    public void showRetry() {

    }
    public void showNoTaskNameError(){
        mTaskNameErrorLabel.setVisibility(View.VISIBLE);}
    public void hideNoTaskNameError(){
        mTaskNameErrorLabel.setVisibility(View.GONE);}
}
