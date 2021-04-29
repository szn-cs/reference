
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
.L0:		# epilogue
	lw    $ra, $fp, 0
	move  $t0, $fp
	lw    $fp, $fp, -4
	move  $sp, $t0
	jr    $ra
