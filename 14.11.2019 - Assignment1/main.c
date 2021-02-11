#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include<stdbool.h>
#include <ctype.h>

typedef struct Matrix{
    char* name;
    int** matrix;
    int row;
    int column;
}Matrix;

typedef struct Vector{
    char* name;
    int length;
    int* vector;
}Vector;

typedef struct node{
    Matrix data;
    struct node* next;
}node;

typedef struct nodeV{
    Vector data;
    struct nodeV* next;
}nodeV;

void append(struct node** head_ref, Matrix new_data){
    /* 1. allocate node */
    node* new_node = (struct node*) malloc(sizeof(struct node));
    node *last = *head_ref;  /* used in step 5*/
    /* 2. put in the data  */
    new_node->data  = new_data;
    /* 3. This new node is going to be the last node, so make next
          of it as NULL*/
    new_node->next = NULL;
    /* 4. If the Linked List is empty, then make the new node as head */
    if (*head_ref == NULL){
        *head_ref = new_node;
        return;
    }
    /* 5. Else traverse till the last node */
    while (last->next != NULL)
        last = last->next;
    /* 6. Change the next of last node */
    last->next = new_node;
    return;
}

void appendv(struct nodeV** head_ref, Vector new_data){
    /* 1. allocate node */
    nodeV* new_node = (struct nodeV*) malloc(sizeof(struct nodeV));
    nodeV *last = *head_ref;  /* used in step 5*/
    /* 2. put in the data  */
    new_node->data  = new_data;
    /* 3. This new node is going to be the last node, so make next
          of it as NULL*/
    new_node->next = NULL;
    /* 4. If the Linked List is empty, then make the new node as head */
    if (*head_ref == NULL){
        *head_ref = new_node;
        return;
    }
    /* 5. Else traverse till the last node */
    while (last->next != NULL)
        last = last->next;
    /* 6. Change the next of last node */
    last->next = new_node;
    return;
}

bool searchName(node* head, char* x){
    // Base case
    if (head == NULL)
        return false;
    // If key is present in current node, return true
    if (strcmp(x,head->data.name) != 0)
        return true;
    // Recur for remaining list
    return searchName(head->next, x);
}

Matrix search(node* head, char* x){
    // If key is present in current node, return true
    if (strcmp(x,head->data.name) == 0)
        return head->data;
    // Recur for remaining list
    return search(head->next, x);
}

void printList(node *node){
    while (node != NULL){
        printf(" %s ", node->data.name);
        node = node->next;
    }
}

int largest(const int arr[], int n)
{
    int i;
    // Initialize maximum element
    int max = arr[0];
    // Traverse array elements from second and
    // compare every element with current max
    for (i = 1; i < n; i++)
        if (arr[i] > max)
            max = arr[i];
    return max;
}

int smallest(const int arr[], int n)
{
    int i;
    // Initialize maximum element
    int min = arr[0];
    // Traverse array elements from second and
    // compare every element with current max
    for (i = 1; i < n; i++)
        if (arr[i] < min)
            min = arr[i];
    return min;
}

int maxi_col(int** mat, int rows, int i){
    int j;
        int maxm = mat[0][i];
        for(j = 0; j < rows; j++){
            if(mat[j][i] > maxm){
                maxm = mat[j][i];
            }
        }
        return maxm;
    }

int mini_col(int** mat, int rows, int i){
    int j;
    int minm = mat[0][i];
    for(j = 0; j < rows; j++){
        if(mat[j][i] < minm){
            minm = mat[j][i];
        }
    }
    return minm;
}

Matrix matread(char* matname, char* arguman, char* outputFile){

    FILE *fp;
    FILE*fp0;
    char* matfolder = (char*) malloc((int)(strlen(arguman) + strlen("/") + strlen(matname) + 1) * sizeof(char));
    char* str = (char*) malloc((int)(strlen(matfolder) + 1) * sizeof(char));
    char* str0 =(char*) malloc((int)(strlen(matfolder) + 1) * sizeof(char));
    char *token;
    int rowC = 0;
    int colC = 0;
    size_t i = 0;
    int k;
    Matrix mat;
    int wcount = 0;

    matfolder = strcpy(matfolder, arguman);
    matfolder = strcat(matfolder, "/");
    strtok(matname, "\n\r");
    matfolder = strcat(matfolder, matname);

    fp = fopen(matfolder, "r");

    while (fgets(str, (int)strlen(matfolder), fp) != NULL){
        rowC++;
        token = strtok(str," ");
        for (i = 0; i < strlen(token); i++) {
            colC++;
        }
    }
    rewind(fp);

    mat.name = (char*) malloc((int)(strlen(matname) + 1));
    strcpy(mat.name,strtok(matname, "."));
    mat.row = rowC;
    mat.column = colC;
    mat.matrix = (int **)malloc(rowC * sizeof(int*));
    for (i=0; i<rowC; i++)
        mat.matrix[i] = (int *)malloc(colC * sizeof(int));

    while (fgets(str0, (int)strlen(matfolder), fp) != NULL){
        token = strtok(str0, " ");
        for (i = 0; i < mat.row; ++i){
            mat.matrix[wcount][i] = atoi(token);
            token = strtok(NULL, " ");
        }
        wcount++;
    }

    fp0 = fopen(outputFile, "w");

    char* output = (char*) malloc((int)(strlen("read matrix") + strlen(mat.name) + 1));
    strcpy(output, mat.name);
    fprintf (fp0, "read matrix %s.mat %d %d\n", output, mat.row, mat.column);
    for (k = 0; k < mat.row; ++k) {
        for (i = 0; i < mat.column; ++i) {
            fprintf(fp0,"%d ", mat.matrix[k][i]);
        }
        fprintf(fp0,"\n");
    }
    fclose (fp0);
    free(output);
    free(matfolder);
    free(str);
    fclose(fp);
    free(str0);

    return mat;
}

void padding(char* name, int x, int y, char* maxormin, node* Head, char* outputFile) {

    Matrix mat;
    int i, k;
    FILE* fp;

    mat = search(Head, name);

    mat.column += y;
    mat.row += x;

    mat.matrix[mat.row-1] = (int*)calloc(mat.column, sizeof(int));



    if(strstr(maxormin, "max") != NULL){
        for (i = 0; i < mat.row - 1; ++i) {
            mat.matrix[i][mat.column - 1] = largest(mat.matrix[i], mat.column - 1);
        }
        for (i = 0; i < mat.column; ++i) {
            mat.matrix[mat.row - 1][i] = maxi_col(mat.matrix, mat.row - 1, i);
        }
    }else{
        for (i = 0; i < mat.row - 1; ++i) {
            mat.matrix[i][mat.column - 1] = smallest(mat.matrix[i], mat.column - 1);
        }
        for (i = 0; i < mat.column; ++i) {
            mat.matrix[mat.row - 1][i] = mini_col(mat.matrix, mat.row - 1, i);
        }
    }

    fp = fopen(outputFile, "a");

    char* output = (char*) malloc((int)(strlen(mat.name) + 1 + strlen("matrix padded")));
    strcpy(output, mat.name);
    fprintf (fp, "matrix paded %s %d %d\n", output, mat.row, mat.column);
    for (k = 0; k < mat.row; ++k) {
        for (i = 0; i < mat.column; ++i) {
            fprintf(fp,"%d ", mat.matrix[k][i]);
        }
        fprintf(fp,"\n");
    }

    fclose(fp);
    free(output);
}

Vector vecread(char* vecname, char* arguman, char* outputFile) {

    FILE *fp;
    FILE *fp0;
    char *vecfolder = (char *) malloc((int) (strlen(arguman) + strlen("/") + strlen(vecname) + 1) * sizeof(char));
    char *token;
    Vector vec;
    int length = 0;
    int i, k;
    char* ptr;

    vecfolder = strcpy(vecfolder, arguman);
    vecfolder = strcat(vecfolder, "/");
    strtok(vecname, "\n\r");
    vecfolder = strcat(vecfolder, vecname);

    char *str = (char*) malloc((int) (strlen(vecfolder) + 1) * sizeof(char));
    char *str0 = (char*) malloc((int) (strlen(vecfolder) + 1) * sizeof(char));

    fp = fopen(vecfolder, "r");

    while (fgets(str, (int) strlen(vecfolder), fp) != NULL) {
        token = strtok(str, " ");
        while (token != NULL) {
            token = strtok(NULL, " ");
            length++;
        }
    }

    vec.length = length;

    vec.name = (char *) malloc((int) (strlen(vecname) + 1));
    strcpy(vec.name, strtok(vecname, "."));
    vec.vector = (int *) malloc(length * sizeof(int));

    rewind(fp);

    while (fgets(str0, (int) strlen(vecfolder), fp) != NULL) {
        token = strtok(str0, " ");
        for (i = 0; i < length; ++i) {
            vec.vector[i] = (int) strtol(token, &ptr, 10);
            token = strtok(NULL, " ");
        }
    }

    fclose(fp);

    fp0 = fopen(outputFile, "a");

    char* output = (char*) malloc((strlen(vec.name) + 1));
    strcpy(output, vec.name);
    fprintf (fp0, "read vector %s.vec %d\n", output, length);
    for (k = 0; k < length; ++k) {
            fprintf(fp0,"%d ", vec.vector[k]);
    }
    fprintf(fp0,"\n");

    fclose (fp0);
    free(str);
    free(output);
    free(str0);
    free(vecfolder);

    return vec;
}

int main(int argc, char **argv) {

        node *Head = NULL;
        nodeV *HeadV = NULL;
        Matrix temp;
        Vector tempV;
        int count = 0;
        char *token;
        char *name = NULL;
        char *mm = NULL;
        int x = 0, y = 0;

        FILE *fp;
        char *str = (char *) malloc((int) (strlen(argv[2]) + 1) * sizeof(char));

        fp = fopen(argv[2], "r");

        while (fgets(str, (int) strlen(argv[2]), fp) != NULL) {
            if (strstr(str, "matread") != NULL) {
                strtok(str, " \n\r");
                token = strtok(NULL, " ");
                temp = matread(token, argv[1], argv[3]);
                append(&Head, temp);
            }else if (strstr(str, "pad") != NULL) {
                strtok(str, " \n\r");
                token = strtok(NULL, " ");
                while (token != NULL) {
                        if (count == 0) {
                            name = token;
                        }
                        if (count == 1) {
                            x = atoi(token);
                        }
                        if (count == 2) {
                            y = atoi(token);
                        }
                        if (count == 3) {
                            mm = token;
                        }
                        count++;
                        token = strtok(NULL, " ");
                    }
                printf("%s", str);
                padding(name, x, y, mm, Head, argv[3]);
            }else if(strstr(str, "vecread") != NULL){
                strtok(str, " \n\r");
                token = strtok(NULL, " ");
                tempV = vecread(token, argv[1], argv[3]);
                appendv(&HeadV, tempV);
            }
        }
        fclose(fp);
        free(str);
        return 0;
    }
