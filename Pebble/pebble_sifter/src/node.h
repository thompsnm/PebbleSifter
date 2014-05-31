#include <stdlib.h>

struct node {
  const char *text;
  struct node *next;
};

void addNode(struct node *newNow, const char *string);

void freeNodes(struct node *root);

struct node* getNodeAtIndex(struct node *root, int index);

int size(struct node *root);