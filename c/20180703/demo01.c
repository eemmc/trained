#include <stdio.h>
#include <stdint.h>
#include <stdlib.h>
#include <string.h>

struct _node{
	int value;
	struct _node *next;
};

struct _list{
	struct _node *data;
	size_t size;
};

void list_add(struct _list *list, int value)
{
	struct _node *item=(struct _node *)malloc(sizeof(struct _node));
	item->value=value;
	
	item->next=list->data;
	list->data=item;

	list->size+=1;
}

void list_sort(struct _list *list)
{
	struct _node *prev;
	struct _node *curr;
	struct _node *temp;
	struct _node *swap=NULL;

	int diff;
	LOOP:while(list->data!=NULL)
	{
		temp=list->data;
		list->data=temp->next;
		if(swap==NULL)
		{
			temp->next=swap;
			swap=temp;
			continue;
		}

		diff=temp->value - swap->value;
		if(diff<=0)
		{
			temp->next=swap;
			swap=temp;
			continue;
		}
		
		for(prev=swap,curr=prev->next;curr!=NULL;curr=curr->next)
		{
			diff=temp->value - curr->value;
			if(diff<=0)
			{
				prev->next=temp;
				temp->next=curr;
				goto LOOP;
			}
			
			prev=curr;
		}
			
		prev->next=temp;
		temp->next=NULL;
	}

	list->data=swap;
}

int main(int argc, char **argv)
{
	struct _list list;
	list.data=NULL;
	list.size=0;

	list_add(&list,1);
	list_add(&list,8);
	list_add(&list,2);
	list_add(&list,7);
	list_add(&list,6);
	list_add(&list,9);
	list_add(&list,4);
	list_add(&list,11);
	list_add(&list,5);
	list_add(&list,7);
	

	list_sort(&list);
	struct _node *item;
	for(item=list.data;item!=NULL;item=item->next)
	{
		fprintf(stderr,"item->value => %d \n",item->value);
	}

	return 0;
}
