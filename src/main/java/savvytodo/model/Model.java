package savvytodo.model;

import java.time.DateTimeException;
import java.util.Set;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import savvytodo.commons.core.UnmodifiableObservableList;
import savvytodo.commons.exceptions.IllegalValueException;
import savvytodo.model.operations.exceptions.RedoFailureException;
import savvytodo.model.operations.exceptions.UndoFailureException;
import savvytodo.model.task.DateTime;
import savvytodo.model.task.ReadOnlyTask;
import savvytodo.model.task.Task;
import savvytodo.model.task.TaskType;
import savvytodo.model.task.UniqueTaskList;
import savvytodo.model.task.UniqueTaskList.DuplicateTaskException;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskManager newData);

    /** Returns the TaskManager */
    ReadOnlyTaskManager getTaskManager();

    /** Deletes the given task */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given task */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;


    /** Checks for tasks with conflicting datetime and returns a string of all conflicting tasks
     * @throws IllegalValueException
     * @throws DateTimeException */
    String getTaskConflictingDateTimeWarningMessage(DateTime dateTimeToCheck)
            throws DateTimeException, IllegalValueException;

    //@@author A0140016B
    /** Returns the filtered task list as an {@code UnmodifiableObservableList<ReadOnlyTask>} */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

    //@@author A0147827U
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList(TaskType taskType);
    ObservableList<ReadOnlyTask> getFilteredEventTaskList();
    ObservableList<ReadOnlyTask> getFilteredFloatingTaskList();
    /**
     * Updates the task located at {@code filteredTaskListIndex} with {@code editedTask}.
     * {@code originalTask} is used to determine the targeted list view
     * @throws DuplicateTaskException if updating the task's details causes the task to be equivalent to
     *      another existing task in the list.
     * @throws IndexOutOfBoundsException if {@code filteredTaskListIndex} < 0 or >= the size of the filtered list.
     */
    void updateTask(int filteredTaskListIndex, ReadOnlyTask originalTask, ReadOnlyTask editedTask)
            throws UniqueTaskList.DuplicateTaskException;
    void updateTask(int filteredTaskListIndex, ReadOnlyTask editedTask) throws DuplicateTaskException;

    /** Returns the total count of all filtered lists*/
    int getTotalFilteredListSize();

    /** Updates the filter of the filtered task list to show all tasks */
    void updateFilteredListToShowAll();

    //@@author A0124863A
    /** Updates the filter of the filtered task list to filter by the given predicate*/
    void updateFilteredTaskList(Predicate<ReadOnlyTask> predicate);

    /** Updates the filter of the filtered task list to filter by the given keywords*/
    void updateFilteredTaskList(Set<String> keywords);

    //@@author A0124863A
    /** Undo an operation */
    void undo() throws UndoFailureException;

    //@@author A0124863A
    /** Redo an operation */
    void redo() throws RedoFailureException;

    //@@author A0124863A
    /** Record a mark or unmark for undo*/
    void recordMark(int index);

}
