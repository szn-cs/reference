	.data
	.align 2		# align on a word boundary
_x:	.space 4
	.data
	.align 2		# align on a word boundary
_y:	.space 4

########################
# ⨍	f
########################
	.text
_f:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 16
	subu  $sp, $sp, 8
#———Body———————————————
	b     .L1
.L1:		# case: true
	li    $t0, 1
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
.L2:		# case: false
	li    $t0, 0
	sw    $t0, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	lw    $v0, 4($sp)	# POP
	addu  $sp, $sp, 4
	b     .L0		# jump: epilogue
#———Exit———————————————
.L0:		# epilogue
	lw    $ra, $fp, -8
	move  $t0, $fp
	lw    $fp, $fp, -12
	move  $sp, $t0
	li    $v0, 10
	syscall

########################
# ⨍	main
########################
	.text
	.global main
main:	
#———Entry———————————————
	sw    $ra, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	sw    $fp, 0($sp)	# PUSH
	subu  $sp, $sp, 4
	addu  $fp, $sp, 8
	subu  $sp, $sp, 0
#———Body———————————————
#———Exit———————————————
.L3:		# epilogue
	lw    $ra, $fp, 0
	move  $t0, $fp
	lw    $fp, $fp, -4
	move  $sp, $t0
	jr    $ra
