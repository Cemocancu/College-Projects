#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
//defining 4 different nodes to accomplish our objective
typedef struct children_3{
    int type;
    void *children0;
    void *children1;
    void *children2;
}children_3; //node with 3 children

typedef struct children_2{
    int type;
    void *children0;
    void *children1;
}children_2; //node with 2 children

typedef struct children_1{
    int type;
    void *children0;
}children_1; //node with 3 children

typedef struct children_0{
    int type;
    char *data;
}children_0; //node to hold the values from read files

//3 different functions to create nodes
children_3 *newNode3(){
    children_3 *node = (children_3 *)malloc(sizeof(children_3));

    node->type = 3;
    node->children0 = NULL;
    node->children1 = NULL;
    node->children2 = NULL;
    return(node);
} //to create node with 3 children

children_2 *newNode2(){
    children_2 *node = (children_2 *)malloc(sizeof(children_2));

    node->type = 2;
    node->children0 = NULL;
    node->children1 = NULL;
    return(node);
} //to create node with 2 children

children_1 *newNode1(){
    children_1 *node = (children_1 *)malloc(sizeof(children_1));

    node->type = 1;
    node->children0 = NULL;
    return(node);
} //to create node with 1 children

children_0 *newNode(){
    children_0 *node = (children_0 *) malloc(sizeof(children_0));

    node->type = 0;
    node->data = NULL;
    return(node);
} //to create the node which holds the values

//declaring function early to use in expression2
void expression3(children_3 *head, int op_length, int pre_op_length, char** op, char** pre_op, int var_length, char **var);

//3 different expression function to create tree recursively
//function to create expression with 1 child
void expression1(children_1 *head, int var_length, char **var){
    int a;

    children_0 *child = NULL;
    child = newNode();
    child->data = (char *) malloc(sizeof(char) * 100);
    a = rand();
    child->data = var[a % var_length];
    head->children0 = child;
}

//function to create expression with 2 child
void expression2(children_2 *head, int op_length, int pre_op_length, char **op, char **pre_op, int var_length, char **var){
    int a;

    children_0 *child = NULL;
    child = newNode();
    child->data = (char *) malloc(sizeof(char) * 100);
    a = rand();
    child->data = pre_op[a % pre_op_length];
    head->children0 = child;
    if(rand() % 3 == 0){
        children_3 *child1 = NULL;
        child1 = newNode3();
        expression3(child1, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children1 = child1;
    }else if(rand() % 3 == 1){
        children_2 *child1 = NULL;
        child1 = newNode2();
        expression2(child1, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children1 = child1;
    }else{
        children_1 *child1 = NULL;
        child1 = newNode1();
        expression1(child1, var_length, var);
        head->children1 = child1;
    }
}

//function to create expression with 3 child
void expression3(children_3 *head, int op_length, int pre_op_length, char** op, char **pre_op, int var_length, char **var){
    int a;

    a =  rand();
    if(a % 3 == 0){
        children_3 *child0 = NULL;
        child0 = newNode3();
        expression3(child0, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children0 = child0;
    }else if(a % 3 == 1){
        children_2 *child0 = NULL;
        child0 = newNode2();
        expression2(child0, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children0 = child0;
    }else{
        children_1 *child0 = NULL;
        child0 = newNode1();
        expression1(child0, var_length, var);
        head->children0 = child0;
    }
    children_1 *child1 = NULL;
    child1 = newNode1();
    children_0 *child = NULL;
    child = newNode();
    child1->children0 = child;
    child->data = (char *) malloc(sizeof(char) * 100);
    a = rand();
    child->data = op[a % op_length];
    head->children1 = child1;
    a = rand();
    if(a % 3 == 0){
        children_3 *child2 = NULL;
        child2 = newNode3();
        expression3(child2, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children2 = child2;
    }else if(a % 3 == 1){
        children_2 *child2 = NULL;
        child2 = newNode2();
        expression2(child2, op_length, pre_op_length, op, pre_op, var_length, var);
        head->children2 = child2;
    }else{
        children_1 *child2 = NULL;
        child2 = newNode1();
        expression1(child2, var_length, var);
        head->children2 = child2;
    }
}

//condition function to loop recursively with root of the tree
void condition(children_3 *head, int set_op_length, int rel_op_length, int op_length, int pre_op_length,
        char ** set_op, char** rel_op, char** op, char **pre_op, int var_length, char **var){
    int a;

    if(rand() % 2 == 0){ //(<cond><set-op><cond>)
        children_3 *child0 = NULL;
        child0 = newNode3();
        children_1 *child1 = NULL;
        child1 = newNode1();
        children_0 *child = NULL;
        child = newNode();
        child1->children0 = child;
        child->data = (char *) malloc(sizeof(char) * 100);
        a = rand();
        child->data = set_op[a % set_op_length];
        children_3 *child2 = NULL;
        child2 = newNode3();
        condition(child0, set_op_length, rel_op_length, op_length, pre_op_length, set_op, rel_op, op, pre_op, var_length, var);
        condition(child2, set_op_length, rel_op_length, op_length, pre_op_length, set_op, rel_op, op, pre_op, var_length, var);
        head->children0 = child0;
        head->children1 = child1;
        head->children2 = child2;
    }else{ //(<expr><rel-op><expr>)
        a = rand();
        if(a % 3 == 0){
            children_3 *child0 = NULL;
            child0 = newNode3();
            expression3(child0, op_length, pre_op_length, op, pre_op, var_length, var);
            head->children0 = child0;
        }else if(a % 3 == 1){
            children_2 *child0 = NULL;
            child0 = newNode2();
            expression2(child0, op_length, pre_op_length, op, pre_op, var_length, var);
            head->children0 = child0;
        }else{
            children_1 *child0 = NULL;
            child0 = newNode1();
            expression1(child0, var_length, var);
            head->children0 = child0;
        }
        children_1 *child1 = NULL;
        child1 = newNode1();
        children_0 *child = NULL;
        child = newNode();
        child1->children0 = child;
        child->data = (char *) malloc(sizeof(char) * 100);
        a = rand();
        child->data = rel_op[a % rel_op_length];
        head->children1 = child1;
        a = rand();
        if(a % 3 == 0){
            children_3 *child2 = NULL;
            child2 = newNode3();
            expression3(child2, op_length, pre_op_length, op, pre_op, var_length, var);
            head->children2 = child2;
        }else if(a % 3 == 1){
            children_2 *child2 = NULL;
            child2 = newNode2();
            expression2(child2, op_length, pre_op_length, op, pre_op, var_length, var);
            head->children2 = child2;
        }else{
            children_1 *child2 = NULL;
            child2 = newNode1();
            expression1(child2, var_length, var);
            head->children2 = child2;
        }
    }
}

//function to print the node that holds the value
void print_tree1(children_0 *head){
    printf("%s", head->data);
}

//function to start looping over nodes recursively to print
void print_tree3(children_3 *head){
    int tmp_type;
    tmp_type = head->type;
    if(tmp_type == 3){
        printf("(");
        print_tree3(head->children0);
        print_tree3(head->children1);
        print_tree3(head->children2);
        printf(")");
    }else if(tmp_type == 2){
        print_tree1(head->children0);
        printf("(");
        print_tree3(head->children1);
        printf(")");
    }else{
        print_tree1(head->children0);
    }
}

int main() {
    time_t t;
    int i, r = 10, c = 100, op_length = 0, pre_op_length = 0, rel_op_length = 0;
    int set_op_length = 0, var_length = 0;
    srand((unsigned) time(&t));//declaring the seed for rand()
    children_3 *root = NULL;
    root = newNode3();//creating root of the tree
    char *line = (char*) malloc(100 * sizeof(char));//array to store file data line by line

    //start of the file reading process
    char **op = (char **) malloc(r * sizeof(char *));//to store file op's content
    for (i = 0; i < r; ++i) {
        op[i] = (char *) malloc(c * sizeof(char));
    }
    FILE *fp =  fopen("op", "r");
    while(fgets(line, sizeof(line), fp) != NULL){//reads op line by line and stores the data to op array
        strcpy(op[op_length], line);
        op_length++;
    }
    fclose(fp);


    char **pre_op = (char **) malloc(r * sizeof(char *));//to store file pre_op's content
    for (i = 0; i < r; ++i) {
        pre_op[i] = (char *) malloc(c * sizeof(char));
    }
    fp = fopen("pre_op", "r");
    while(fgets(line, sizeof(line), fp) != NULL){//reads pre_op line by line and stores the data to pre_op array
        strcpy(pre_op[pre_op_length], line);
        pre_op_length++;
    }
    fclose(fp);


    char **rel_op = (char **) malloc(r * sizeof(char *));
    for (i = 0; i < r; ++i) {
        rel_op[i] = (char *) malloc(c * sizeof(char));
    }
    fp = fopen("rel_op", "r");
    while(fgets(line, sizeof(line), fp) != NULL){
        strcpy(rel_op[rel_op_length], line);
        rel_op_length++;
    }
    fclose(fp);


    char **set_op = (char **) malloc(r * sizeof(char *));
    for (i = 0; i < r; ++i) {
        set_op[i] = (char *) malloc(c * sizeof(char));
    }
    fp = fopen("set_op", "r");
    while(fgets(line, sizeof(line), fp) != NULL){
        strcpy(set_op[set_op_length], line);
        set_op_length++;
    }
    fclose(fp);


    char **var = (char **) malloc(r * sizeof(char *));
    for (i = 0; i < r; ++i) {
        var[i] = (char *) malloc(c * sizeof(char));
    }
    fp = fopen("var", "r");
    while(fgets(line, sizeof(line), fp) != NULL){
        strcpy(var[var_length], line);
        var_length++;
    }
    fclose(fp);

    //start of 5 different loop in order to remove \n from the arrays
    for (i = 0; i < op_length; ++i) {
        op[i][strlen(op[i]) - 1] = '\0';
    }

    for (i = 0; i < pre_op_length; ++i) {
        pre_op[i][strlen(pre_op[i]) - 1] = '\0';
    }

    for (i = 0; i < rel_op_length; ++i) {
        rel_op[i][strlen(rel_op[i]) - 1] = '\0';
    }

    for (i = 0; i < set_op_length; ++i) {
        set_op[i][strlen(set_op[i]) - 1] = '\0';
    }

    for (i = 0; i < var_length; ++i) {
        var[i][strlen(var[i]) - 1] = '\0';
    }

    //starting the creation loop recursively
    condition(root, set_op_length, rel_op_length, op_length, pre_op_length, set_op, rel_op, op, pre_op, var_length, var);
    printf("if(");
    print_tree3(root);
    printf(") {}\n");
    return 0;
}