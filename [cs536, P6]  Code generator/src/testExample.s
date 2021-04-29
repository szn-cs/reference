	.data
	.align 2		# align on a word boundary
_numValidInputs:	.space 4

########################
# ⨍	main
########################
	.text
	.globl main
main:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 0
#———Body———————————————
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	la    $t0, _numValidInputs
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	lw    $t1, 4($sp)	# POP
	addu  $sp, $sp, 4
	sw    $t1, 0($t0)	# assign to address
	sw    $t1, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $t0, 4($sp)	# POP
	addu  $sp, $sp, 4
	.data
.L1:	.asciiz "Program is starting...\n"	# string
	.text
	la    $t0, .L1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	.data
.L2:	.asciiz "Program is terminating...\n"	# string
	.text
	la    $t0, .L2
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $a0, 4($sp)	# POP
	addu  $sp, $sp, 4
	li    $v0, 4
	syscall		# write
	b     .L0		# jump: epilogue
#———Exit———————————————
.L0:		# epilogue
	lw    $ra, 0($fp)
	move  $t0, $fp
	lw    $fp, $fp, -4
	move  $sp, $t0
	jr    $ra
