#include <stdlib.h>
#include <stdio.h>

struct node {
  void* data;
  struct node* prev;
};

static struct node* head = NULL;
static struct node* tail = NULL;

int empty() {
  return (head == NULL) ? 0 : -1;
}

void push(void* data) {
  if(head == NULL) {
    head = malloc(sizeof(struct node));
    head->data = data;
    head->prev = NULL;
    tail = head;
  } else {
    struct node* temp = malloc(sizeof(struct node));
    temp->data = data;
    temp->prev = tail;
    tail = temp;
  }
}

void* pop() {
  if(empty() == 0) {
    return NULL;
  } else {
    void* data = tail->data;
    struct node* temp = tail;
    tail = tail->prev;
    if(tail == NULL) {
      head = NULL;
    }
    free(temp);
  }
}

void dump() {
  struct node* temp = tail;
  printf("dump stack\n");
  while(temp != NULL) {
    printf("0x%08x ", (unsigned int)(temp->data));
  }
  printf("\n");
}
