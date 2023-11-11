package com.example.datetimepickerapp.views

import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.datetimepickerapp.R
import com.example.datetimepickerapp.dal.db.Database
import com.example.datetimepickerapp.dal.repository.EmployeeMockRepository
import com.example.datetimepickerapp.getScreenWidth
import com.example.datetimepickerapp.ui.theme.DateTimePickerAppTheme
import com.example.datetimepickerapp.ui.theme.components.CustomAlertDialog
import kotlinx.coroutines.launch


import java.time.LocalDate


class DateTimeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val employeeRepo = Database.getDatabase(this).employeeDao()

        val dateTimeMainActivityViewModel =
            DateTimeMainActivityViewModel(EmployeeMockRepository(employeeRepo))

        setContent {
            DateTimePickerAppTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainPage(dateTimeMainActivityViewModel)
                }
            }
        }
    }
}


@Composable
fun MainPage(viewModel: DateTimeMainActivityViewModel) {
    val coroutineScope = rememberCoroutineScope()

    val weekDayCount = viewModel.getWeekDates()
    val dateOfMonthWidth = getScreenWidth() / weekDayCount.size

    var showShrinkAnimation by remember { mutableStateOf(false) }


    ConstraintLayout(Modifier.fillMaxSize()) {

        val (datePicker, timepicker, submitButton) = createRefs()
        val guideLine1f = createGuidelineFromTop(0.15f)
        val guideLine9f = createGuidelineFromTop(0.9f)



        when {
            viewModel.showErrorMessageSQLConstraint -> {
                ShowToastMessage(message = stringResource(R.string.already_checked_in))
            }

            viewModel.showUnsupportedOperationException -> {
                ShowToastMessage(message = stringResource(R.string.you_cannot_check_in_in_the_future))
            }
        }

        DatePicker(
            daysOfWeek = weekDayCount,
            viewModel = viewModel,
            itemSize = dateOfMonthWidth,
            modifier = Modifier
                .constrainAs(datePicker) {
                    top.linkTo(guideLine1f)
                    centerHorizontallyTo(parent)
                })


        TimePicker(
            viewModel = viewModel,
            shrinkButton = showShrinkAnimation,
            onPressedChange = {
                showShrinkAnimation = true

            },
            modifier = Modifier
                .constrainAs(timepicker) {
                    bottom.linkTo(submitButton.top, 20.dp)
                    centerHorizontallyTo(parent)
                })


        SubmitButton(
            action = {
                coroutineScope.launch {
                    viewModel.postDateTime()
                }
                showShrinkAnimation = false

            },
            checkInInfo = viewModel.getSelectedDateAsString(),
            modifier = Modifier
                .constrainAs(submitButton) {
                    bottom.linkTo(guideLine9f)
                    centerHorizontallyTo(parent)
                })


    }

}


@Composable
fun DatePicker(
    daysOfWeek: List<LocalDate>,
    viewModel: DateTimeMainActivityViewModel,
    itemSize: Int,
    modifier: Modifier
) {


    ConstraintLayout(modifier = modifier) {
        val (weekDays, dateString, weekNumber, arrowButtons) = createRefs()


        Text(text = viewModel.getSelectedDateAsString(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start,
            modifier = Modifier.constrainAs(dateString) {
                start.linkTo(parent.start, 10.dp)
                bottom.linkTo(weekDays.top, 10.dp)

            })

        Row(modifier = Modifier.constrainAs(arrowButtons) {
            end.linkTo(parent.end, 10.dp)
            bottom.linkTo(weekDays.top, 10.dp)

        }) {
            DatePickerIcon(itemSize * 1.4, Icons.Filled.KeyboardArrowLeft,
                action = { viewModel.goToPreviousWeek() }
            )
            DatePickerIcon(
                itemSize * 1.4, Icons.Filled.KeyboardArrowRight,
                color = getArrowIconColor(isInitialWeek = viewModel.isInitialWeek()),
                action = { viewModel.goToNextWeek() },
                isInitialWeek = viewModel.isInitialWeek()
            )
        }



        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .constrainAs(weekDays) {
                    top.linkTo(arrowButtons.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            items(daysOfWeek) { dateOfMonth ->
                DayOfMonthButton(
                    itemSize, dateOfMonth.dayOfMonth,
                    color = getButtonColor(
                        dateOfMonth,
                        viewModel.selectedDate,
                        viewModel.currentDate
                    )
                ) {
                    viewModel.changeSelectedDay(
                        dateOfMonth
                    )
                }
            }
        }

        Text(text = "Week ${viewModel.getWeekNumber()}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(weekNumber) {
                top.linkTo(weekDays.bottom)
                centerHorizontallyTo(weekDays)

            })


    }


}


@Composable
fun TimePicker(
    viewModel: DateTimeMainActivityViewModel,
    shrinkButton: Boolean,
    onPressedChange: () -> Unit,
    modifier: Modifier
) {
    val context = LocalContext.current


    val buttonSize by animateDpAsState(
        targetValue = if (shrinkButton) 50.dp else getScreenWidth().dp / 3,
        animationSpec = tween(durationMillis = 1000), label = ""
    )


    val timePicker = TimePickerDialog(
        context,
        { _,
          selectedHour: Int,
          selectedMinute: Int ->
            viewModel.setTimeByHourMinutes(selectedHour, selectedMinute)
            onPressedChange()
        }, viewModel.selectedTime.hour, viewModel.selectedTime.minute, true
    )


    Button(
        onClick = {
            timePicker.show()
        },
        shape = CircleShape,
        contentPadding = PaddingValues(5.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        modifier = modifier.size(buttonSize)
    ) {

        if (shrinkButton) {
            Icon(
                Icons.Rounded.Schedule,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        } else {
            Text(
                text = stringResource(R.string.select_time),
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SubmitButton(action: () -> Unit, checkInInfo: String, modifier: Modifier) {
    var showDialog by remember { mutableStateOf(false) }


    CustomAlertDialog(
        isOpened = showDialog,
        onClose = { showDialog = !showDialog },
        onConfirm = {
            showDialog = !showDialog
            action()
        },
        title = stringResource(R.string.confirm_check_in), text = checkInInfo
    )
    Button(
        onClick = { showDialog = true }, modifier = modifier
            .height(100.dp)
            .width(300.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primary),
        contentPadding = PaddingValues(5.dp)
    ) {
        Text(
            text = stringResource(R.string.submit),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun getButtonColor(dayOfMonth: LocalDate, selectedDay: LocalDate, today: LocalDate): Color {
    if (dayOfMonth > today) {
        return Color.Gray
    }
    if (dayOfMonth == selectedDay) {
        return Color(0xFF406E8E)
    }
    return MaterialTheme.colorScheme.primary
}


@Composable
private fun getArrowIconColor(isInitialWeek: Boolean): Color {
    if (isInitialWeek) {
        return Color.Gray
    }
    return MaterialTheme.colorScheme.primary
}


@Composable
private fun DatePickerIcon(
    size: Double,
    icon: ImageVector,
    color: Color = MaterialTheme.colorScheme.primary,
    action: () -> Unit,
    isInitialWeek: Boolean = false
) {
    Icon(
        icon,
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .clickable {
                if (!isInitialWeek) {
                    action()
                }
            },
        tint = color
    )
}

@Composable
private fun DayOfMonthButton(itemWidth: Int, dayOfMonth: Int, color: Color, action: () -> Unit) {


    Button(
        onClick = { action() },
        Modifier
            .size(itemWidth.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(color),
        contentPadding = PaddingValues(5.dp)
    ) {
        Text(
            dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@Composable
fun ShowToastMessage(message: String) {
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}






