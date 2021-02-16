.text
main:	addi $sp, $sp, -4 	#make room on stack to save $ra
	sw $ra, 0($sp)		#save $ra on the stack
	addi $s0, $s0, 5	#declare $s0 = a, $s1 = b, $s3 = result
	addi $s1, $s1, 3
	addi $s3, $s3, 0
	bne $s0, $s1, else	#if not equal jump to else
	multu $s0, $s1		#multiples a with b
	mflo $s3		#loads the product to result
	j done			#jump to done
	
	else:	jal assess		#jump to assess
		add $s3, $s3, $v0	#put $v0 to $s3
		j done			#jump to done
	
	assess:	addi $sp, $sp, -4	#make room on stack to save $ra
		sw $ra, 0($sp)		#save $ra on stack
		slt $t0, $s1, $s0	#if b less than a $t0 is 1
		bne $t0, 1 assesselse	#if $t0 is not 1 jump assesselse
		jal upgrade		#jump to upgrade
		lw $ra, 0($sp)		#restore $ra
		addi, $sp, $sp, 4	#deallocate stack space
		jr $ra			#return to else
		
	assesselse:	jal demote		#jump to demote
			lw $ra, 0($sp)		#restore $ra
			addi, $sp, $sp, 4	#deallocate stack space
			jr $ra			#return to else

	upgrade:	addi $sp, $sp, -4	#make room on stack
			sw $ra, 0($sp)		#save $ra on stack
			addu $t1, $s1, $s0	#put a+b into $t2
			addi $t2, $t2, 4	#to use $t2 as 4 in another operation
			multu $t1, $t2		#multiple $t1 and $t2
			mflo $v0		#put result to $v0
			lw $ra, 0($sp)		#restore $ra
			addi, $sp, $sp, 4	#deallocate stack space
			jr $ra			#return to assess
				
	demote:	addi $sp, $sp, -4	#make room on stack
		sw $ra, 0($sp)		#save $ra on stack
		subu $t1, $s1, $s0	#put b-a into $t1
		addi $t2, $t2, 4	#to use $t2 as 4 in another operation
		multu $t1, $t2		#multiple $t1 and $t2
		mflo $v0		#put result to $v0
		lw $ra, 0($sp)		#restore $ra
		addi, $sp, $sp, 4	#deallocate stack space
		jr $ra			#return to assesselse
	
	done:	lw $ra, 0($sp) 		#restore $ra
		addi $sp, $sp, 4	#deallocate stack space
		jr $ra			#exit program