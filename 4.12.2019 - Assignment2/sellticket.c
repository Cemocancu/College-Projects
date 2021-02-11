#include <stdio.h>
#include <stdlib.h>
#include <string.h>

typedef struct node {
    int data;
    int priority;
    struct node* next;
} Node; //From here to addseat it is priority queue functions and creation

Node* newNode(int d, int p){
    Node* temp = (Node*)malloc(sizeof(Node));
    temp->data = d;
    temp->priority = p;
    temp->next = NULL;

    return temp;
}

int peek(Node** head){
    return (*head)->data;
}

void pop(Node** head){
    Node* temp = *head;
    (*head) = (*head)->next;
    free(temp);
}

int isEmpty(Node** head){
    return (*head) == NULL;
}

void push(Node** head, int d, int p){
    Node* start = (*head);
    Node* temp = newNode(d, p);

    if(!isEmpty(head)){
        if ((*head)->priority > p) {
            temp->next = *head;
            (*head) = temp;
        }
        else {
            while (start->next != NULL && start->next->priority < p) {
                start = start->next;
            }
            temp->next = start->next;
            start->next = temp;
        }
    }else{
        temp->next = *head;
        (*head) = temp;
    }
}  //priority queue end

void addseat(char* str, char** flight_names, int *flight_index, int** flight_seats,  //when addseat occurs in input file this functions is called
        int max_flight_names, char** closed_flight_names, FILE** fptr, char* outputfile){

    char *token;  //definitons start
    int count = 0, i, j, count0 = 0, error = 0;
    token = strtok(str, " \n\r");
    *fptr = fopen(outputfile, "a");
    int flight_counter = -1;
    int tmp_flight_index = 0;
    int tmp = 0; //definitons end

    while (token != NULL) { //it loops over addseat functions parameters
        if (count != 0) {
            if(count == 1){
                for(i = 0; i < max_flight_names; i++){ // checks if flight is created
                    if(strcmp(flight_names[i], token) == 0){
                        tmp_flight_index = i;
                        flight_counter++;
                    }
                }
                if(flight_counter == -1){
                    for(j = 0; j < max_flight_names; j++){ //checks if flight is closed
                        if(strcmp(closed_flight_names[j], token) == 0){
                            fprintf(*fptr, "error\n");
                            error += 1;
                            break;
                        }else{
                            count0++;
                        }
                    }
                    if(count0 == max_flight_names){
                        strcpy(flight_names[*flight_index], token);
                        tmp_flight_index = *flight_index;
                        *flight_index += 1;
                    }
                }
            }else if(count == 2){ // checks what type of seat will be added or created
                if(error == 0){
                    if(strstr(token, "business") != NULL){
                        tmp = 0;
                    }
                    else if(strstr(token, "economy") != NULL){
                        tmp = 1;
                    }
                    else if(strstr(token, "standard") != NULL){
                        tmp = 2;
                    }else{
                        fprintf(*fptr, "error\n");
                        break;
                    }
                }
            }else if(count == 3){
                if(error == 0){ // inserts the number of seats to the array
                    if((int) strtol(token, (char **)NULL, 10) > 0){
                        flight_seats[tmp_flight_index][tmp] += (int) strtol(token, (char **)NULL, 10);
                    }else{
                        fprintf(*fptr, "error\n");
                    }
                }
            }
        }
        count++;
        token = strtok(NULL, " \n\r");
    }

    fprintf(*fptr, "addseats %s", flight_names[tmp_flight_index]); // writes to outputfile
    for(i = 0; i < 3; i ++){
        fprintf(*fptr, " %d",flight_seats[tmp_flight_index][i]);
    }
    fprintf(*fptr, "\n");
    fclose(*fptr);
}

void enqueue(char* str, char** flight_names, Node** priority_queue_b, //when enqueue occurs in input file this functions is called
        Node** priority_queue_e, Node** priority_queue_s, int* passenger_index,
        int* hbb, int* hbb0, char** passenger_names_b, char** passenger_names_e, char** passenger_names_s,
        int** bq, int** eq, int** sq, char* f_name, char* tmp, char* b_e_s, char** closed_flight_names,
        int max_flight_names, int* still_in_queue, char** flight_names_b, char** flight_names_e,
        char** flight_names_s, FILE** fptr, char* outputfile){

    char* token;
    *fptr = fopen(outputfile, "a");
    int i, j;
    token = strtok(str, " \n\r");
    int count = 0, count0 = 0, error = 0;
    int tmp_flight_index = 0;

    while (token != NULL) {
        if(count == 1){
            strcpy(f_name, token);
            for(i = 0; i < max_flight_names; i++){
                if(strcmp(flight_names[i], token) != 0){
                    count0++;
                }else{
                    tmp_flight_index = i;
                    for(j = 0; j < max_flight_names; j++){
                        if(strcmp(closed_flight_names[j], f_name) == 0){
                            error += 1;
                            break;
                        }
                    }
                }
            }
            if(count0 == max_flight_names){
                fprintf(*fptr, "error\n");
                break;
            }
        }
        if(count == 2){
            if(error == 0){
                strcpy(b_e_s, token);
                if(strstr(token, "business") != NULL){ // if enqueued ticket is business
                    token = strtok(NULL, " \n\r");
                    strcpy(tmp, token);
                    token = strtok(NULL, "\n\r");
                    if(token != NULL){
                        if(strstr(token, "diplomat") != NULL){ // if passenger is diplomat
                            push(priority_queue_b, *passenger_index, *hbb);
                            *hbb += 1;
                            strcpy(passenger_names_b[*passenger_index], tmp);
                            strcpy(flight_names_b[*passenger_index], f_name);
                            fprintf(*fptr, "queue %s %s %s %d\n",f_name, tmp, b_e_s, bq[tmp_flight_index][0] + 1);
                            *still_in_queue += 1;
                            bq[tmp_flight_index][0] += 1;
                            *passenger_index += 1;
                        }else{
                            fprintf(*fptr, "error\n");
                        }
                    }else{
                        push(priority_queue_b, *passenger_index, *hbb0); // if passenger is normal
                        *hbb0 += 1;
                        strcpy(passenger_names_b[*passenger_index], tmp);
                        strcpy(flight_names_b[*passenger_index], f_name);
                        fprintf(*fptr, "queue %s %s %s %d\n",f_name, tmp, b_e_s, bq[tmp_flight_index][0] + 1);
                        *still_in_queue += 1;
                        bq[tmp_flight_index][0] += 1;
                        *passenger_index += 1;
                    }
                }
                else if(strstr(token, "economy") != NULL){  // if enqueued ticket is economy
                    token = strtok(NULL, " \n\r");
                    strcpy(tmp, token);
                    token = strtok(NULL, "\n\r");
                    if(token != NULL){
                        if(strstr(token, "veteran") != NULL){// if passenger is veteran
                            push(priority_queue_e, *passenger_index, *hbb);
                            *hbb += 1;
                            strcpy(passenger_names_e[*passenger_index], tmp);
                            strcpy(flight_names_e[*passenger_index], f_name);
                            fprintf(*fptr, "queue %s %s %s %d\n",f_name, tmp, b_e_s, eq[tmp_flight_index][0] + 1);
                            *still_in_queue += 1;
                            eq[tmp_flight_index][0] += 1;
                            *passenger_index += 1;
                        }else{
                            fprintf(*fptr, "error\n");
                        }
                    }else{ // if passenger is normal
                        push(priority_queue_e, *passenger_index, *hbb0);
                        *hbb0 += 1;
                        strcpy(passenger_names_e[*passenger_index], tmp);
                        strcpy(flight_names_e[*passenger_index], f_name);
                        fprintf(*fptr, "queue %s %s %s %d\n",f_name, tmp, b_e_s, eq[tmp_flight_index][0] + 1);
                        *still_in_queue += 1;
                        eq[tmp_flight_index][0] += 1;
                        *passenger_index += 1;
                    }
                }
                else if(strstr(token, "standard") != NULL){  // if enqueued ticket is standard
                    token = strtok(NULL, " \n\r");
                    strcpy(tmp, token);
                    token = strtok(NULL, "\n\r");
                    if(token != NULL){
                        fprintf(*fptr, "error\n");
                        break;
                    }else{
                        push(priority_queue_s, *passenger_index, *hbb0);
                        *hbb0 += 1;
                        strcpy(passenger_names_s[*passenger_index], tmp);
                        strcpy(flight_names_s[*passenger_index], f_name);
                        fprintf(*fptr, "queue %s %s %s %d\n",f_name, tmp, b_e_s, sq[tmp_flight_index][0] + 1);
                        *still_in_queue += 1;
                        sq[tmp_flight_index][0] += 1;
                        *passenger_index += 1;
                    }
                }else{
                    fprintf(*fptr, "error\n");
                }
            }
        }
        count++;
        token = strtok(NULL, " \n\r");
    }
    fclose(*fptr);
}

void sell(char* str,int max_flight_names, char** flight_names, int** flight_seats, Node** priority_queue_b, //when sell occurs in input file
        Node** priority_queue_e, Node** priority_queue_s, char** sold_passenger_names_b,
        char** sold_passenger_names_e, char** sold_passenger_names_s, int* sold_passenger_index,
        char** passenger_names_b, char** passenger_names_e, char** passenger_names_s, int* sold_b,
        int* sold_e, int* sold_s, char** closed_flight_names, FILE** fptr, char* outputfile,
        int** bq, int** eq, int** sq){

    char* token;
    int count = 0, error = 0;
    int i, j, k;
    *fptr = fopen(outputfile, "a");

    token = strtok(str, " \n\r");
    while (token != NULL) {
        if(count == 1){
            for(i = 0; i < max_flight_names; i++){
                if(strcmp(token, flight_names[i]) == 0){
                    for (k = 0; k < max_flight_names; ++k) {
                        if(strcmp(closed_flight_names[k], token) == 0){
                            error += 1;
                            break;
                        }
                    }
                    if(error == 0){
                        for (j = 0; j < flight_seats[i][0] + 1; ++j) { // looks over priority queue to sell
                            if(*sold_b < flight_seats[i][0]){
                                if(isEmpty(priority_queue_b)){
                                    break;
                                }else if(peek(priority_queue_b) == -1){
                                    pop(priority_queue_b);
                                }else{
                                    strcpy(sold_passenger_names_b[*sold_passenger_index], passenger_names_b[peek(priority_queue_b)]);
                                    *sold_b += 1;
                                    bq[i][0] -= 1;
                                    pop(priority_queue_b);
                                    *sold_passenger_index +=1;
                                }
                            }
                        }
                        for (j = 0; j < flight_seats[i][1] + 1; ++j) { // looks over priority queue to sell
                            if(*sold_e < flight_seats[i][1]){
                                if(isEmpty(priority_queue_e)){
                                    break;
                                }else if(peek(priority_queue_e) == -1){
                                    pop(priority_queue_e);
                                } else {
                                    strcpy(sold_passenger_names_e[*sold_passenger_index], passenger_names_e[peek(priority_queue_e)]);
                                    *sold_e += 1;
                                    eq[i][0] -= 1;
                                    pop(priority_queue_e);
                                    *sold_passenger_index += 1;
                                }
                            }
                        }
                        for (j = 0; j < flight_seats[i][2] + 1; ++j) { // looks over priority queue to sell
                            if(*sold_s < flight_seats[i][2]){
                                if(isEmpty(priority_queue_s)){
                                    break;
                                }else if(peek(priority_queue_s) == -1){
                                    pop(priority_queue_s);
                                } else {
                                    strcpy(sold_passenger_names_s[*sold_passenger_index], passenger_names_s[peek(priority_queue_s)]);
                                    *sold_s += 1;
                                    sq[i][0] -= 1;
                                    pop(priority_queue_s);
                                    *sold_passenger_index += 1;
                                }
                            }
                        }
                        while(*sold_s < flight_seats[i][2]){ // looks empty standard seats to sell to bus and eco passengers
                            if(!isEmpty(priority_queue_b)){
                                strcpy(sold_passenger_names_s[*sold_passenger_index], passenger_names_b[peek(priority_queue_b)]);
                                *sold_s += 1;
                                bq[i][0] -= 1;
                                pop(priority_queue_b);
                                *sold_passenger_index += 1;
                            }else if(!isEmpty(priority_queue_e)){
                                strcpy(sold_passenger_names_s[*sold_passenger_index], passenger_names_e[peek(priority_queue_e)]);
                                *sold_s += 1;
                                eq[i][0] -= 1;
                                pop(priority_queue_e);
                                *sold_passenger_index += 1;
                            }else{
                                break;
                            }
                        }
                        fprintf(*fptr, "sold %s %d %d %d\n",flight_names[i], *sold_b, *sold_e, *sold_s);
                        break;
                    }else{
                        fprintf(*fptr, "error\n");
                    }
                    }

            }
        }
        count++;
        token = strtok(NULL, " \n\r");
    }
    fclose(*fptr);
}

void close(char* str, int max_flight_names, char** flight_names, char** closed_flight_names, int* closed_flight_index, // when close occurs in input file
        int sold_b,int sold_e, int sold_s, int still_in_queue, Node** priority_queue_b, Node** priority_queue_e,
        Node** priority_queue_s, char** passenger_names_b, char** passenger_names_e, char** passenger_names_s,
        FILE** fptr, char* outputfile){

    int count0 = 0, i;
    char* token;
    strtok(str, " \n\r");
    token = strtok(NULL, " \n\r");
    *fptr = fopen(outputfile, "a");

    for(i = 0; i < max_flight_names; ++i){
        if(strcmp(flight_names[i], token) == 0){
                if(strcmp(closed_flight_names[i], token) == 0){
                    fprintf(*fptr, "error\n");
                    break;
                }else{
                    strcpy(closed_flight_names[*closed_flight_index], token);
                    *closed_flight_index += 1;
                    fprintf(*fptr, "closed %s %d %d\n", token, sold_b + sold_e + sold_s, still_in_queue - (sold_b + sold_e + sold_s));
                    while (!isEmpty(priority_queue_b)) { //looks over priority queue to pop the passengers waiting
                        fprintf(*fptr, "waiting %s\n", passenger_names_b[peek(priority_queue_b)]);
                        pop(priority_queue_b);
                    }
                    while (!isEmpty(priority_queue_e)) {
                        fprintf(*fptr, "waiting %s\n", passenger_names_e[peek(priority_queue_e)]);
                        pop(priority_queue_e);
                    }
                    while (!isEmpty(priority_queue_s)) {
                        fprintf(*fptr, "waiting %s\n", passenger_names_s[peek(priority_queue_s)]);
                        pop(priority_queue_s);
                    }
                    break;
                }
        }else{
            count0++;
        }
        if(count0 == max_flight_names){
            fprintf(*fptr, "error\n");
        }
    }
    fclose(*fptr);
}

void report(char* str, int max_flight_names, char ** flight_names, int sold_b, int sold_e, int sold_s, //when report occurs in input file
        char** sold_passenger_names_b, char** sold_passenger_names_e, char** sold_passenger_names_s,
        FILE** fptr, char* outputfile){
    char* token;
    int lc = 0;
    int i;
    strtok(str, " \n\r");
    token = strtok(NULL, " \n\r");
    *fptr = fopen(outputfile, "a");

    for (i = 0; i < max_flight_names; i++) {
        if(strcmp(flight_names[i], token) == 0){
            lc++;
            break;
        }
    }
    if(lc > 0){
        fprintf(*fptr, "report %s\nbusiness %d\n", token, sold_b); // looks over flight's passengers to get their information
        for (i = 0; i < max_flight_names; i++){
            if(strcmp(sold_passenger_names_b[i], "") != 0)
                fprintf(*fptr, "%s\n", sold_passenger_names_b[i]);
            }
            fprintf(*fptr, "economy %d\n", sold_e);
            for (i = 0; i < max_flight_names; i++){
                if(strcmp(sold_passenger_names_e[i], "") != 0)
                    fprintf(*fptr, "%s\n", sold_passenger_names_e[i]);
            }
            fprintf(*fptr, "standard %d\n", sold_s);
            for (i = 0; i < max_flight_names; i++){
                if(strcmp(sold_passenger_names_s[i], "") != 0)
                    fprintf(*fptr, "%s\n", sold_passenger_names_s[i]);
            }
            fprintf(*fptr, "end of report %s\n", token);
    }else{
        fprintf(*fptr, "error\n");
    }
    fclose(*fptr);
}

void info(char* str, int max_passenger_names, char** passenger_names_b, char** passenger_names_e,
        char** passenger_names_s, char** flight_names_b, char** flight_names_e, char** flight_names_s,
        char** sold_passenger_names_b, char** sold_passenger_names_e, char** sold_passenger_names_s,
        FILE** fptr, char* outputfile){ // when info occurs in input file
    char* token;
    int i, j, count0 = 0;
    *fptr = fopen(outputfile, "a");

    strtok(str, " \n\r");
    token = strtok(NULL, " \n\r");
    int nc = 0;
    for (i = 0; i < max_passenger_names; ++i) {
        if(strcmp(passenger_names_b[i], token) == 0){
            fprintf(*fptr, "info %s %s business", token, flight_names_b[i]);
            for (j = 0; j < max_passenger_names; j++) { // gets info about business passengers
                if(strcmp(sold_passenger_names_b[j], token) == 0){
                    fprintf(*fptr, " business\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_e[j], token) == 0){
                    fprintf(*fptr, " economy\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_s[j], token) == 0) {
                    fprintf(*fptr, " standard\n");
                    nc++;
                    break;
                }
            }
            if(nc == 0){
                fprintf(*fptr, " none\n");
                break;
            }
        }else if(strcmp(passenger_names_e[i], token) == 0){
            fprintf(*fptr, "info %s %s economy", token, flight_names_e[i]);
            for (j = 0; j < max_passenger_names; j++) {// gets info about economy passengers
                if(strcmp(sold_passenger_names_b[j], token) == 0){
                    fprintf(*fptr, " business\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_e[j], token) == 0){
                    fprintf(*fptr, " economy\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_s[j], token) == 0) {
                    fprintf(*fptr, " standard\n");
                    nc++;
                    break;
                }
            }
            if(nc == 0){
                fprintf(*fptr, " none\n");
                break;
            }
        }else if(strcmp(passenger_names_s[i], token) == 0){
            fprintf(*fptr, "info %s %s standard", token, flight_names_s[i]);
            for (j = 0; j < max_passenger_names; j++) {// gets info about standard passengers
                if(strcmp(sold_passenger_names_b[j], token) == 0){
                    fprintf(*fptr, " business\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_e[j], token) == 0){
                    fprintf(*fptr, " economy\n");
                    nc++;
                    break;
                }else if(strcmp(sold_passenger_names_s[j], token) == 0) {
                    fprintf(*fptr, " standard\n");
                    nc++;
                    break;
                }
            }
            if(nc == 0){
                fprintf(*fptr, " none\n");
                break;
            }
        }else{
            count0++;
        }
    }
    if(count0 == max_passenger_names){
        fprintf(*fptr, "error\n");
    }
    fclose(*fptr);
}

int main(int argc, char **argv) {
    FILE *fp;
    FILE* fptr;
    int i, flight_index = 0, passenger_index = 0, sold_passenger_index = 0, closed_flight_index = 0;
    int hbb = 1, hbb0 = 1500, still_in_queue = 0;
    size_t size = 1500, size0 = 1000;
    char* f_name = (char *) malloc(size * sizeof(char));
    int xyh = 1000, uyt = 3000;
    int sold_b = 0, sold_e = 0, sold_s = 0;
    char* tmp = (char *) malloc(size * sizeof(char));
    char* b_e_s = (char *) malloc(size * sizeof(char));
    char *str = (char *) malloc(size * sizeof(char));
    char *filename = argv[1];
    int max_flight_names = xyh, max_flight_seats = uyt, max_passenger_names = uyt;
    //array declerations begin
    char** flight_names = (char**)malloc(sizeof(char*) * max_flight_names);
    for (i = 0; i < max_flight_names; i++){
        flight_names[i] = (char*)malloc(size0 * sizeof(char));
    }
    int** flight_seats = (int**)malloc(sizeof(int*) * max_flight_seats);
    for (i = 0; i < max_flight_seats; i++){
        flight_seats[i] = (int*)calloc(3, sizeof(int));
    }
    char** flight_names_b = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        flight_names_b[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** flight_names_e = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        flight_names_e[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** flight_names_s = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        flight_names_s[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** passenger_names_b = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        passenger_names_b[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** passenger_names_e = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        passenger_names_e[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** passenger_names_s = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        passenger_names_s[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** sold_passenger_names_b = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; ++i) {
        sold_passenger_names_b[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** sold_passenger_names_e = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; ++i) {
        sold_passenger_names_e[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** sold_passenger_names_s = (char**)malloc(sizeof(char*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; ++i) {
        sold_passenger_names_s[i] = (char*)malloc(size0 * sizeof(char));
    }
    char** closed_flight_names = (char**)malloc(sizeof(char*) * max_flight_names);
    for (i = 0; i < max_flight_names; ++i) {
        closed_flight_names[i] = (char*) malloc(size0 * sizeof(char));
    }
    int** bq = (int**)malloc(sizeof(int*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        bq[i] = (int*)malloc(size0 * sizeof(int));
    }
    int** eq = (int**)malloc(sizeof(int*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        eq[i] = (int*)malloc(size0 * sizeof(int));
    }
    int** sq = (int**)malloc(sizeof(int*) * max_passenger_names);
    for (i = 0; i < max_passenger_names; i++){
        sq[i] = (int*)malloc(size0 * sizeof(int));
    }
    //array declaretions end
    //priority queue declaretions begin
    Node* priority_queue_b = newNode(-1,0);
    Node* priority_queue_e = newNode(-1,0);
    Node* priority_queue_s = newNode(-1,0);
    //priority queue declaretions end

    fp = fopen(filename, "r+");
    while (fgets(str, size, fp) != NULL) { // loops over input file line by line and calls the correct functions
        if (strstr(str, "addseat") != NULL) {
            addseat(str, flight_names, &flight_index, flight_seats, max_flight_names, closed_flight_names, &fptr, argv[2]);
        }
        else if(strstr(str, "enqueue") != NULL){
            enqueue(str, flight_names, &priority_queue_b, &priority_queue_e, &priority_queue_s,
                    &passenger_index, &hbb, &hbb0,passenger_names_b, passenger_names_e,
                    passenger_names_s, bq, eq, sq, f_name, tmp, b_e_s, closed_flight_names, max_flight_names,
                    &still_in_queue, flight_names_b, flight_names_e, flight_names_s, &fptr, argv[2]);
        }else if(strstr(str, "sell") != NULL){
            sell(str, max_flight_names, flight_names, flight_seats, &priority_queue_b, &priority_queue_e,
                    &priority_queue_s, sold_passenger_names_b, sold_passenger_names_e, sold_passenger_names_s,
                    &sold_passenger_index, passenger_names_b, passenger_names_e, passenger_names_s, &sold_b,
                    &sold_e, &sold_s, closed_flight_names, &fptr, argv[2], bq, eq, sq);
        }else if(strstr(str, "close") != NULL){
            close(str, max_flight_names, flight_names, closed_flight_names, &closed_flight_index,sold_b,
                    sold_e, sold_s, still_in_queue, &priority_queue_b, &priority_queue_e, &priority_queue_s,
                    passenger_names_b, passenger_names_e, passenger_names_s, &fptr, argv[2]);
        }else if(strstr(str, "report") != NULL){
            report(str, max_flight_names,flight_names, sold_b, sold_e, sold_s, sold_passenger_names_b,
                    sold_passenger_names_e, sold_passenger_names_s, &fptr, argv[2]);
        }else if(strstr(str, "info") != NULL){
            info(str, max_passenger_names, passenger_names_b, passenger_names_e, passenger_names_s,
                    flight_names_b, flight_names_e, flight_names_s, sold_passenger_names_b,
                    sold_passenger_names_e, sold_passenger_names_s, &fptr, argv[2]);
        }
    }
     //free begin
    fclose(fp);
    free(str);
    free(f_name);
    free(b_e_s);
    free(tmp);
    for (i = 0; i < max_flight_names; i++)
    {
        char* currentCharPtr = flight_names[i];
        free(currentCharPtr);
    }
    free(flight_names);
    for (i = 0; i < max_flight_seats; i++)
    {
        int* currentCharPtr0 = flight_seats[i];
        free(currentCharPtr0);
    }
    free(flight_seats);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = passenger_names_b[i];
        free(currentCharPtr);
    }
    free(passenger_names_b);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = passenger_names_e[i];
        free(currentCharPtr);
    }
    free(passenger_names_e);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = passenger_names_s[i];
        free(currentCharPtr);
    }
    free(passenger_names_s);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = sold_passenger_names_b[i];
        free(currentCharPtr);
    }
    free(sold_passenger_names_b);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = sold_passenger_names_e[i];
        free(currentCharPtr);
    }
    free(sold_passenger_names_e);
    for (i = 0; i < max_passenger_names; i++){
        char* currentCharPtr = sold_passenger_names_s[i];
        free(currentCharPtr);
    }
    free(sold_passenger_names_s);
    for (i = 0; i < max_flight_names; i++) {
        char* currentCharPtr = closed_flight_names[i];
        free(currentCharPtr);
    }
    free(closed_flight_names);
    for (i = 0; i < max_passenger_names; i++) {
        char* currentCharPtr = flight_names_b[i];
        free(currentCharPtr);
    }
    free(flight_names_b);
    for (i = 0; i < max_passenger_names; i++) {
        char* currentCharPtr = flight_names_e[i];
        free(currentCharPtr);
    }
    free(flight_names_e);
    for (i = 0; i < max_passenger_names; i++) {
        char* currentCharPtr = flight_names_s[i];
        free(currentCharPtr);
    }
    free(flight_names_s);
    for (i = 0; i < max_passenger_names; i++) {
        int* currentCharPtr = bq[i];
        free(currentCharPtr);
    }
    free(bq);
    for (i = 0; i < max_passenger_names; i++) {
        int* currentCharPtr = eq[i];
        free(currentCharPtr);
    }
    free(eq);
    for (i = 0; i < max_passenger_names; i++) {
        int* currentCharPtr = sq[i];
        free(currentCharPtr);
    }
    free(sq);
    while (!isEmpty(&priority_queue_b)) {
        pop(&priority_queue_b);
    }
    while (!isEmpty(&priority_queue_e)) {
        pop(&priority_queue_e);
    }
    while (!isEmpty(&priority_queue_s)) {
        pop(&priority_queue_s);
    }
    //free end
    return 0;
}
