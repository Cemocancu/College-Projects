#include <stdio.h>
#include <stdlib.h>
int input0, i, counter;
int *array0;
void tribonacci()
{
    array0 = (int*) malloc(input0 * sizeof(int));
    *(array0) = 0;
    *(array0 + 1) = 1;
    *(array0 + 2) = 2;
    counter = 3;
    while (input0 > 3)
    {
        if (counter != input0)
        {
            *(array0 + counter) = *(array0 + counter - 3) + *(array0 + counter - 2) + *(array0 + counter - 1);
            counter++;
        }
        else
            break;
    }
    for (i = 0; i < input0; i++)
    {
        if (i == input0 - 1)
            printf("%d", *(array0 + i));
        else
            printf("%d ", *(array0 + i));
    }
}
int main()
{
    printf("Enter a number: ");
    scanf("%d", &input0);
    tribonacci();
    return 0;
}
