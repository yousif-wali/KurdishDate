import java.time.format.DateTimeFormatter
import java.util.Locale
import java.time.LocalDate
import java.time.Year
import java.time.YearMonth
import java.time.temporal.ChronoUnit

public object KurdishDate{
    /*
        Hello,
        
        This is class is calculating Kurdish year which originates in Mesopotamia
        which in modern day is Kurdistan region and it is located on north of Iraq.

        The calendar starts 700 B.C on March 21st, and it is used among many people
        in middle-east.

        Each month has it is own specific name and days, and each one of them are
        season specific names like:

        01 -> (Newroz)     or "New Day"
        02 -> (Gulan)      or "Flowers are blooming"
        03 -> (Jozardan)   or "Wheats are turning into yellow color"
        04 -> (Pushpar)    or "When winds blow leafs off in a hot day"
        05 -> (Galawezh)   or "A 'Galawezh' star will apear in this month"
        06 -> (Kharmanan)  or "Harvesting month"
        07 -> (Razbar)     or "Vegtables and Fruits growth rate increases"
        08 -> (Gala rezan) or "Falling leafs"
        09 -> (Sarma warz) or "Cold season"
        10 -> (Bafranbar)  or "Usually snows on this month in Mesopotamia region"
        11 -> (Rebandan)   or "Roads and streets will blocked due to heavy rains and snows"
        12 -> (Rashame)    or "Sun will get so hot than tanning process will rate will increase"

    */

    // Returning currect Kurdish Date by calling KurdishDate.getDate();
    fun getDate() : String{
       return ConvertGregorianToKurdish(LocalDate.now())
    }

    // Converts Gregorian Date into Kurdish Date, this method needs a parameter of type LocalDate
    // if not sure how to get LocalDate, then use KurdishDate.getDate(); to get your current calendar
    fun ConvertGregorianToKurdish(localDate: LocalDate) : String{

        // Storing Kurdish months name in order 01->12
        val kurdishMonth : List<String> = listOf("نەورۆز", "گوڵان", "جۆزەردان", "پوشپەڕ", "گەلاوێژ", "خەرمانان", "ڕەزبەر", "گەڵاڕێزان", "سەرماوەرز", "بەفرانبار", "ڕیبەندان", "ڕەشەمێ");

        // Rashame or 12th month has 29 days on non-Leap years and 30 days on Leap years
        val year = Year.of(YearMonth.now().year)
        
        // Getting total days from current year, with two possible answers (365 || 366)
        val totalDays = year.length()

        // First six month of the year has 31 days always,
        val firstSixMonths = 31;
        // Meanwhile seventh month to 11th month has 30 days each.
        val seventhToEleventh = 30;
        // 12th month has 29 days on non-Leap years
        var twelveth = 29;

        // Checking if current year has 366 days , in another word , is it a Leap year.
        if(totalDays == 366){
            twelveth++
        }

        /*  Setting a temporarily variable for Kurdish date

            Note:
                kurdishDate variable holds current year, which makes the algorithm a bit 
                tricky.

                Instead of calculating 700 and check for each Leap and non-Leap year

                I decided to go for current year and then get differences between
                days, months, years
        */
        val kurdishDate = LocalDate.of(localDate.year, 3, 21)

        // Total days between todays gregorian date compared to Kurdish date - 700 years
        var daysDifference = ChronoUnit.DAYS.between(kurdishDate, localDate)

        /*  Here we calculate our days, in case we run into negative values
            which it happens on new year's eve:
                
                the reason is kurdish calendar will restart its cycle on March 21st
                of every year,
                and if we get the difference days, then we get negative values

            the solution to this issue would be to check if we got a negative
            value, then subtract the whole days in the year (Leap years 
            included) from current gregorian date
        */
        var calculateDate : Long;
        if(daysDifference < 0L){
            calculateDate = totalDays - (-daysDifference);
        }else{
            calculateDate = daysDifference;
        }

        // After getting the correct amount of days we then loop over each month. starting 
        // from index 0, which will be the first month "Newroz"
        var month = 0;
        var found = false;
        while(!found){
            /*  checking if we subtract the calculateDate is positive after subtracting 
                firstSixMonts which is 31 days. then we will go to the next month
            */
            if(month <= 6 && (calculateDate - firstSixMonths) > 0){
                calculateDate -= firstSixMonths;
                month++;
                
            }
            //  Same concept is happening here but instead of 31, we subtract 30
            if(month < 11 && month >= 6 && (calculateDate - seventhToEleventh) > 0){
                calculateDate -= seventhToEleventh;
                month++;              
            }
            // Finally we will subtract either 29 or 30 when we reach twelveth month
            if(month > 11){
                calculateDate -= twelveth;
                month++;
            }
            /*  if the remaining days are less than 29 then we stop our loop, because 
                none of the months have 29 days.
            */
            if(calculateDate <= 29){
                found = true;
            }
        }

        /*  Here we calculate kurdish year by checking for 700 B.C or 699 B.C
                remember:
                    if we are in January then we will be 699 B.C until we get to March 21st
                    then kurdish calendar will get its 700 B.C
        */
        var kurdishYear : Int = localDate.year;
        if(month > 9 && month < 3){
            kurdishYear += 700;
        }else{
            kurdishYear += 699;
        }
        // Returning the calculated fields
        return String.format("%d ,%d %S", kurdishYear, calculateDate, kurdishMonth[month]);
    }
}