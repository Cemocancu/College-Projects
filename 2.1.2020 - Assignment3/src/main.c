#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <limits.h>

//defining struct types for gen, chromosome and population
typedef struct gen{
    char value;
    struct gen* next;
}gen_t;

typedef struct chromosome{
    gen_t* gen;
    double rank;
    int fitness;
    int order;
    struct chromosome* next;
}chromosome_t;

typedef struct population{
    chromosome_t* chromosome;
}population_t;

//defining struct selection, xover and mutate to store file contents
typedef struct selection{
    char first_select_chromosome;
    char second_select_chromosome;
    int generation;
    struct selection *next;
}selection_t;

typedef struct xover{
    int first_select_gen;
    int second_select_gen;
    int generation;
    struct xover *next;
}xover_t;

typedef struct mutate{
    int mutation_gen;
    int generation;
    struct mutate *next;
}mutate_t;

//function to push gen to a chromosome
void pushGen(chromosome_t** chromosome, char value){

    gen_t* current = (*chromosome)->gen;

    while(current->next != NULL){
        current = current->next;
    }
    current->next = malloc(sizeof(gen_t));
    current->next->value = value;
    current->next->next = NULL;
}

//function to print population
void print_All(population_t* population, int* count,
        int POP_SIZE){

    int i;
    chromosome_t* currentChromosome = population->chromosome;
    chromosome_t* tmp = currentChromosome;

    for(i = 0; i < POP_SIZE; i++){
        currentChromosome = tmp;
        while(currentChromosome != NULL){
            gen_t* currentGen = currentChromosome->gen;
            if(currentChromosome->order == *count){
                *count += 1;
                while(currentGen != NULL){
                    if(currentGen->next != NULL){
                        printf("%c:", currentGen->value);
                        currentGen = currentGen->next;
                    }else{
                        printf("%c -> %d\n", currentGen->value, currentChromosome->fitness);
                        currentGen = currentGen->next;
                    }
                }
            }
            currentChromosome = currentChromosome->next;
        }
    }
}

//Function to print a single chromosome
void print_chromosome(chromosome_t* chromosome, int PROB_SIZE){

    int i;
    gen_t *current_gen = chromosome->gen;

    for (i = 0; i < PROB_SIZE; ++i) {
        if(i != PROB_SIZE - 1){
            printf("%c:", current_gen->value);
        }else{
            printf("%c -> %d\n", current_gen->value, chromosome->fitness);
        }
        current_gen = current_gen->next;
    }
}

//This function initialize the population while reading from population file
void initialization(FILE* fp, chromosome_t* currentChromosome,
        int PROB_SIZE, int POP_SIZE) {

    char ch;
    int first = 1;
    int countGen = 0;
    int countChromosome = 0;

    //reads the file character by character
    while((ch = (char) fgetc(fp)) != EOF){
        if(ch != ':'){
            //malloc the next gen for the chromosome
            if(first == 1){
                currentChromosome->gen = malloc(sizeof(gen_t));
                currentChromosome->gen->value = ch;
                currentChromosome->gen->next = NULL;
                countGen++;
                first = 0;
            }
                //checks so that each chromosome has PROB_SIZE gens
            else if(countGen != PROB_SIZE){
                pushGen(&currentChromosome, ch);
                countGen++;
                //checks so that population has POP_SIZE chromosomes
            }else if(countChromosome == POP_SIZE){
                break;
                //creates new chromosome when previous chromosomes' gen size reaches PROB_SIZE
            }else{
                currentChromosome->next = malloc(sizeof(chromosome_t));
                currentChromosome = currentChromosome->next;
                currentChromosome->next = NULL;
                currentChromosome->order = 0;
                currentChromosome->rank = -1;
                countChromosome++;
                countGen = 0;
                first = 1;
            }
        }
    }

}

//Function to evaluate fitness of the chromosomes
void fitnessEvaluation(population_t* population, int PROB_SIZE){

    int count = 1, fitness = 0;
    gen_t* currentGen;
    chromosome_t* currentChromosome = population->chromosome;

    //loops for POP_SIZE and PROB_SIZE
    while(currentChromosome != NULL){
        currentGen = currentChromosome->gen;
        while(currentGen != NULL){
            if(currentGen->value - '0' == 1){
                fitness += (int) pow(2, PROB_SIZE - count);
            }
            count++;
            currentGen = currentGen->next;
        }
        currentChromosome->fitness = fitness;
        count = 1;
        fitness = 0;
        currentChromosome = currentChromosome-> next;
    }
}

//Function to sort linked list
void sortList(chromosome_t* head, int* order, int POP_SIZE){

    int i, j;
    chromosome_t* tmp = head;

    //iterates over linked list to assign order to them
    for(i = 0; i < POP_SIZE; i++){
        head = tmp;
        int min_value = INT_MAX;
        for(j = 0; j < POP_SIZE; j++){
            if(head->fitness < min_value){
                if(head->order == 0)
                min_value = head->fitness;
            }
            head = head->next;
        }
        head = tmp;
        for(j = 0; j < POP_SIZE; j++){
            if(head->fitness == min_value){
                if(head->order == 0){
                    head->order = *order;
                    *order += 1;
                }
            }
            head = head->next;
        }
    }
}

//Function to find best fitness for rank calculation
double findBestFitness(population_t* population, int POP_SIZE){

    int i;
    double best_fitness = INT_MAX;
    chromosome_t *current_chromosome = population->chromosome;

    for(i = 0; i < POP_SIZE; i++) {
        if(best_fitness > (double) current_chromosome->fitness){
            best_fitness = (double) current_chromosome->fitness;
        }
        current_chromosome = current_chromosome->next;
    }

    return best_fitness;
}

//Function to find worst fitness for rank calculation
double findWorstFitness(population_t *population){

    double worst_fitness = INT_MIN;
    chromosome_t *current_chromosome = population->chromosome;

    while(current_chromosome != NULL) {
        if(worst_fitness < (double) current_chromosome->fitness){
            worst_fitness = (double) current_chromosome->fitness;
        }
        current_chromosome = current_chromosome->next;
    }

    return worst_fitness;
}

//Function to calculate rank of the chromosomes
void rankCalculation(population_t *population, int POP_SIZE){

    double rank;
    double best_fitness = findBestFitness(population, POP_SIZE);
    double worst_fitness = findWorstFitness(population);
    chromosome_t* currentChromosome = population->chromosome;

    while(currentChromosome != NULL) {
        if(currentChromosome->rank == -1){
            rank = (currentChromosome->fitness - best_fitness) / (worst_fitness - best_fitness);
            currentChromosome->rank = rank;
        }
        currentChromosome = currentChromosome->next;
    }
}

//Function to find the best chromosome
void find_best_chromosome(population_t *population, chromosome_t* best_chromosome, int PROB_SIZE, int POP_SIZE){

    chromosome_t *current_chromosome = population->chromosome;
    double epsilon = 0.0000000000000000000000000000001;
    int i, j;

    for (j = 0; j < POP_SIZE; ++j) {
        if((current_chromosome->rank - 0.0000000000) < epsilon){
            printf("Best chromosome found so far: ");
            if(best_chromosome->fitness > current_chromosome->fitness){
                best_chromosome->fitness = current_chromosome->fitness;
                best_chromosome->gen = malloc(sizeof(gen_t));
                gen_t *best_gen = best_chromosome->gen;
                gen_t *current_gen = current_chromosome->gen;
                for (i = 0; i < PROB_SIZE; ++i) {
                    best_gen->value = current_gen->value;
                    best_gen->next = malloc(sizeof(gen_t));
                    best_gen = best_gen->next;
                    current_gen = current_gen->next;
                }
                break;
            }/*checks if best chromosome of this population is better than best chromosome found so far*/
        }
        current_chromosome = current_chromosome->next;
    }
}

//Reads the file selection and stores the values to selection linked list
void read_selection(FILE *fp, selection_t *selection){

    char ch = (char) fgetc(fp);
    int generation = 1;

    while(ch != EOF){
        if(ch != '\n'){
            if(ch != ' '){
                if(ch != ':'){
                    selection->generation = generation;
                    selection->first_select_chromosome = ch;
                    ch = (char) fgetc(fp);
                }else{
                    ch = (char) fgetc(fp);
                    selection->second_select_chromosome = ch;
                    ch = (char) fgetc(fp);
                }
            }else{
                selection->next = malloc(sizeof(selection_t));
                selection = selection->next;
                ch = (char) fgetc(fp);
            }
        }else{
            selection->next = malloc(sizeof(selection_t));
            generation++;
            selection = selection->next;
            ch = (char) fgetc(fp);
        }
    }
}

//Reads the file xover and stores the values to xover linked list
void read_xover(FILE *fp, xover_t *xover){

    char ch = (char) fgetc(fp);
    int generation = 1;

    while(ch != EOF){
        if(ch != '\n'){
            if(ch != ':'){
                xover->generation = generation;
                xover->first_select_gen = ch - '0';
                ch = (char) fgetc((fp));
                while(ch != ':'){
                    xover->first_select_gen *= 10;
                    xover->first_select_gen += ch -'0';
                    ch = (char) fgetc(fp);
                }
            }else{
                ch = (char) fgetc((fp));
                xover->second_select_gen = ch - '0';
                ch = (char) fgetc((fp));
                while(ch != '\n'){
                    xover->second_select_gen *= 10;
                    xover->second_select_gen += ch -'0';
                    ch = (char) fgetc(fp);
                }
            }
        }else{
            xover->next = malloc(sizeof(xover_t));
            generation++;
            xover = xover->next;
            ch = (char) fgetc((fp));
        }
    }
}

//Reads the file mutate and stores the values to mutate linked list
void read_mutate(FILE *fp, mutate_t *mutate){

    char ch = (char) fgetc(fp);
    int generation = 1;

    while(ch != EOF){
        if(ch != '\n'){
            mutate->generation = generation;
            mutate->mutation_gen = ch - '0';
            ch = (char) fgetc(fp);
        }else{
            mutate->next = malloc(sizeof(mutate_t));
            generation++;
            mutate = mutate->next;
            ch = (char) fgetc(fp);
        }
    }

}

int main(int argc, char* argv[]) {

    //defining command line arguments
    int PROB_SIZE = (int) strtol(argv[1], NULL, 10);
    int POP_SIZE = (int) strtol(argv[2], NULL, 10);
    int MAX_GEN = (int) strtol(argv[3], NULL, 10);

    FILE* fp;

    /*order is the order of chromosomes while count is checking in print_All function
    to print chromosomes in right order*/
    int order = 1, count = 1;
    int i;

    //stores the best chromosome found so far
        chromosome_t* best_chromosome_found = malloc(sizeof(chromosome_t));
    best_chromosome_found->fitness = INT_MAX;

    //defining population
    population_t* population = NULL;
    population = malloc(sizeof(population_t));
    population->chromosome = malloc(sizeof(chromosome_t));
    chromosome_t* currentChromosome = population->chromosome;
    currentChromosome->next = NULL;
    currentChromosome->order = 0;
    currentChromosome->rank = -1;


    int generation = 0;//keeps track of generation

    //defining linked lists to store file contents
    selection_t *selection = NULL;
    selection = malloc(sizeof(selection_t));
    xover_t *xover = NULL;
    xover = malloc(sizeof(xover_t));
    mutate_t *mutate = NULL;
    mutate = malloc(sizeof(mutate_t));

    /* START */

    //open population file
    fp = fopen("population", "r");
    //initialize the population
    initialization(fp, currentChromosome, PROB_SIZE, POP_SIZE);
    //close the file pointer to use again for a different file
    fclose(fp);


    //open selection file
    fp = fopen("selection", "r");
    //read and store selection file
    read_selection(fp, selection);
    //close the file pointer to use again for a different file
    fclose(fp);


    //open xover file
    fp = fopen("xover", "r");
    read_xover(fp, xover);
    //close the file pointer to use again for a different file
    fclose(fp);


    //open mutate file
    fp = fopen("mutate", "r");
    read_mutate(fp, mutate);
    //close the file pointer
    fclose(fp);


    //evaluate the fitness' of chromosomes
    fitnessEvaluation(population, PROB_SIZE);


    //sort the chromosomes
    sortList(currentChromosome, &order, POP_SIZE);
    order = 1;


    //calculate ranks of the chromosomes
    rankCalculation(population, POP_SIZE);


    //generation 0
    printf("GENERATION: 0\n");
    //print population
    print_All(population, &count, POP_SIZE);
    count = 1;


    //find and store best chromosome found so far
    find_best_chromosome(population, best_chromosome_found, PROB_SIZE,POP_SIZE);
    //print best found chromosome
    print_chromosome(best_chromosome_found, PROB_SIZE);


    //loop for repetitive xover and mutation
    while(generation != MAX_GEN){

        generation++;
        selection_t *current_selection = selection;
        chromosome_t *current_chromosome0 = currentChromosome;
        printf("GENERATION: %d\n", generation);//print current generation

        chromosome_t *first_select_chromosome = NULL; //store the selected chromosomes
        chromosome_t *second_select_chromosome = NULL;

        /*This loop finds the selected chromosomes in selection
         * file to use for xover and mutation*/
        while(current_selection->next != NULL){
            if(current_selection->generation == generation){//find current generation's selected chromosomes

                chromosome_t *current_chromosome = currentChromosome;

                while(current_chromosome != NULL){
                    if(current_chromosome->order == current_selection->first_select_chromosome - '0'){
                        first_select_chromosome = current_chromosome;
                        break;
                    }//find selected first chromosome and store it
                    current_chromosome = current_chromosome->next;
                }
                current_chromosome = currentChromosome;
                while(current_chromosome != NULL){
                    if(current_chromosome->order == current_selection->second_select_chromosome - '0'){
                        second_select_chromosome = current_chromosome;
                        break;
                    }//find selected second chromosome and store it
                    current_chromosome = current_chromosome->next;
                }

                xover_t *current_xover = xover;

                /*This loop finds the selected chromosomes' xover gens
                * and swaps them*/
                while(current_xover->next != NULL){
                    if(current_xover->generation == generation){//find current generation's xover gens
                        gen_t *first_current_gen = first_select_chromosome->gen;
                        gen_t *second_current_gen = second_select_chromosome->gen;
                        char tmp_gen;
                        for (i = 1; i < current_xover->first_select_gen; ++i) {
                            first_current_gen = first_current_gen->next;
                            second_current_gen = second_current_gen->next;
                        }//iterate over gens to go to selected gens
                        for (i = 0; i < current_xover->second_select_gen - current_xover->first_select_gen + 1; ++i) {
                            tmp_gen = first_current_gen->value;
                            first_current_gen->value = second_current_gen->value;
                            second_current_gen->value = tmp_gen;
                            first_current_gen = first_current_gen->next;
                            second_current_gen = second_current_gen->next;
                        }//swap gen values
                        break;
                    }
                    current_xover = current_xover->next;
                }

                mutate_t *current_mutate = mutate;

                /*This loop find the selected chromosomes' mutation gens and changes them*/
                while(current_mutate != NULL){
                    if(current_mutate->generation == generation){
                        gen_t *first_current_gen = first_select_chromosome->gen;
                        gen_t *second_current_gen = second_select_chromosome->gen;
                        for (i = 1; i < current_mutate->mutation_gen; ++i) {
                            first_current_gen = first_current_gen->next;
                            second_current_gen = second_current_gen->next;
                        }//iterate over gens to go to selected gens
                        if(first_current_gen->value - '0' == 0){
                            first_current_gen->value = '1';
                        }else{
                            first_current_gen->value = '0';
                        }
                        if(second_current_gen->value - '0' == 0){
                            second_current_gen->value = '1';
                        }else{
                            second_current_gen->value = '0';
                        }//swap gen values
                        break;
                    }
                    current_mutate = current_mutate->next;
                }
            }
            current_selection = current_selection->next;
        }

        for (i = 0; i < POP_SIZE; ++i) {
            current_chromosome0->order = 0;
            current_chromosome0->rank = -1;
            current_chromosome0 = current_chromosome0->next;
        }//make all chromosomes' order 0 and rank -1 because that is how i implemented my code

        /*repeated fitness and rank evaluation,
         * sorting and printing list and finding
         * best chromosome storing it and printing it*/
        fitnessEvaluation(population, PROB_SIZE);
        sortList(currentChromosome, &order, POP_SIZE);
        order = 1;
        rankCalculation(population, POP_SIZE);
        print_All(population, &count, POP_SIZE);
        count = 1;
        find_best_chromosome(population, best_chromosome_found, PROB_SIZE,POP_SIZE);
        print_chromosome(best_chromosome_found, PROB_SIZE);
    }
    return 0;
}
