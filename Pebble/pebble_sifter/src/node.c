#include <stdlib.h>

struct node {
  const char *text;
  struct node *next;
};

void initNode(struct node *newNode, const char *string) {
  newNode = malloc(sizeof(struct node) + sizeof(string));
  newNode->text = string;
  newNode->next = 0;
}

void addNode(struct node *root, const char *string) {
  if(root == NULL) {
    initNode(root, string);
  } else {
    struct node *newNode = NULL;
    initNode(newNode, string);

    struct node *iter = root;
    while (iter->next != 0) {
      iter = iter->next;
    }
    iter->next = newNode;
  }
}

void freeNode(struct node *oldNode) {
  oldNode->text = 0;
  oldNode->next = 0;
  free(oldNode);
}

void freeNodes(struct node *root) {
  if (root == 0) {
    return;
  }
  struct node *nextNode = root->next;
  freeNode(root);
  freeNodes(nextNode);
}

struct node* getNodeAtIndex(struct node *root, int index) {
  if (index == 0) {
    return root;
  } else if (root->next == 0) {
    //TODO: Throw error
    return NULL;
  } else {
    index--;
    return getNodeAtIndex(root->next, index);
  }
}

size_t size(struct node *root) {
  int size = 0;
  struct node *iter = root;
  while (iter != 0) {
    iter = iter->next;
    size++;
  }
  return size;
}