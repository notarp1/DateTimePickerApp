package com.example.datetimepickerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.datetimepickerapp.ui.theme.DateTimePickerAppTheme

import java.time.LocalDate


class DateTimeMainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val dateTimeMainActivityViewModel = DateTimeMainActivityViewModel()

        setContent {
            DateTimePickerAppTheme(dynamicColor = false){
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
fun MainPage(viewModel: DateTimeMainActivityViewModel){



    ConstraintLayout(Modifier.fillMaxSize()){

        val  (datePicker) = createRefs()


        val daysOfSelectedWeek = viewModel.getWeekDays()

        DatePicker(daysOfWeek = daysOfSelectedWeek,
            viewModel = viewModel,
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(datePicker) {
                    centerVerticallyTo(parent)
                    centerHorizontallyTo(parent)
                })



    }

}


@Composable
fun DatePicker(daysOfWeek: List<LocalDate>, viewModel: DateTimeMainActivityViewModel, modifier: Modifier){
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val itemWidth = screenWidth / daysOfWeek.size

    ConstraintLayout(modifier = modifier) {
        val  (daysRef, dateRef, weekRef, buttonsRef) = createRefs()

        Text(text = viewModel.getSelectedDateAsString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(dateRef){
                start.linkTo(parent.start, 10.dp)
                centerVerticallyTo(buttonsRef)

            })

        Row(modifier = Modifier.constrainAs(buttonsRef){
            end.linkTo(parent.end, 10.dp)
        }) {
            DatePickerIcon(itemWidth, Icons.Filled.KeyboardArrowLeft,
                color =  MaterialTheme.colorScheme.primary,
                action = { viewModel.goToPreviousWeek() }
            )
            DatePickerIcon(itemWidth, Icons.Filled.KeyboardArrowRight,
                color =  getArrowIconColor(isInitialWeek = viewModel.isInitialWeek()),
                action = { viewModel.goToNextWeek() },
                isInitalWeek = viewModel.isInitialWeek()
            )
        }



        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .constrainAs(daysRef) {
                    top.linkTo(buttonsRef.bottom)
                    centerHorizontallyTo(parent)
                },
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {

            items(daysOfWeek) { dateOfMonth ->


                DayOfMonthButton(itemWidth, dateOfMonth.dayOfMonth, color  = getButtonColor(dateOfMonth, viewModel.selectedDay, viewModel.today)) {
                    viewModel.changeSelectedDay(
                        dateOfMonth
                    )
                }
            }
        }

        Text(text = "Week ${viewModel.getWeekNumber()}",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(weekRef){
                top.linkTo(daysRef.bottom)
                centerHorizontallyTo(daysRef)

            })
    }


}




@Composable
fun getButtonColor(dayOfMonth: LocalDate, selectedDay: LocalDate, today: LocalDate): Color{
    if(dayOfMonth > today){
        return Color.Gray
    }
    if(dayOfMonth == selectedDay){
        return Color(0xFF406E8E)
    }
    return MaterialTheme.colorScheme.primary
}


@Composable
fun getArrowIconColor(isInitialWeek: Boolean): Color{
    if(isInitialWeek){
        return Color.DarkGray
    }
    return MaterialTheme.colorScheme.primary
}


@Composable
fun DatePickerIcon(size: Int, icon: ImageVector, color: Color, action: () -> Unit, isInitalWeek: Boolean = false){
    Icon(
        icon,
        contentDescription = null,
        modifier = Modifier
            .size(size.dp)
            .clickable {
                if (!isInitalWeek) {
                    action()
                }
            },
        tint = color
    )
}

@Composable
fun DayOfMonthButton(itemWidth: Int, dayOfMonth: Int, color: Color, action: () -> Unit){


    Button(onClick = { action() },
        Modifier
            .size(itemWidth.dp)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(color),
        contentPadding = PaddingValues(5.dp)) {
        Text(dayOfMonth.toString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.CenterVertically))
    }
}












