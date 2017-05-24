#!/bin/sh -login

#PBS -N test_template
#PBS -l walltime=5:00
#PBS -l mem=4gb
#PBS -l nodes=1:ppn=1


#PBS -e pbs.err
#PBS -o pbs.out
#PBS -M samuel.lotz@gmail.com
#PBS -m abe

# ------------------------------
# Script for running the task test_template with PBS on hpcc

# task name, this is the name of the directory for the task
TASK="test_template"
# directory path for this task
TASKDIR="$MY_DICKSON_LAB/pbs_templates/examples/$TASK"

# the directory with all of the input
INPUTDIR=$TASKDIR/input
# directory to put job results in
JOBSDIR=$TASKDIR/jobs
# we should redirect our own output streams but we will catch the PBS
# files as well
PBS_STDOUT=$PBS_O_WORKDIR/pbs.out
PBS_STDERR=$PBS_O_WORKDIR/pbs.err

# the job name
JOBNAME=${TASK}-${PBS_JOBID}
# the directory for the output of this job
JOBDIR=$JOBSDIR/$JOBNAME
# the directory output will be put into after completion
OUTPUTDIR=$JOBDIR/output
# log files
LOG=$JOBDIR/log
# directories to copy the stdout and stderr from scripts
STDOUT=$JOBDIR/pbs.out
STDERR=$JOBDIR/pbs.err

# directory to execute code in
# EXECDIR=$TMPDIR
# EXECDIR=$MY_DICKSON_SCRATCH/$JOBNAME
# Problems with Scratch so I will just run in the jobs dir
# TODO make an if for different options
EXECDIR=$JOBDIR/exec

# make these directories if they do not exist
mkdir -p $JOBDIR
mkdir -p $EXECDIR
mkdir -p $PBS_STDOUT
mkdir -p $PBS_STDERR

# Starting the LOG file
echo "Starting Log file" 1> $LOG 2> $LOG
echo "----------------------------------------" 1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG

# write out explicitly the run parameters
echo "----------------------------------------"  1>> $LOG 2>> $LOG
echo "PBS Resource requests" 1>> $LOG 2>> $LOG
echo "----------------------------------------" 1>> $LOG 2>> $LOG

echo "" 1>> $LOG 2>> $LOG
echo "walltime 5:00" 1>> $LOG 2>> $LOG
echo "memory 4gb" 1>> $LOG 2>> $LOG
echo "num-nodes 1" 1>> $LOG 2>> $LOG
echo "num-processors 1" 1>> $LOG 2>> $LOG
echo "num-gpus " 1>> $LOG 2>> $LOG
echo "feature " 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# print the paths for all the variables constructed in this script
echo "----------------------------------------"  1>> $LOG 2>> $LOG
echo "Check Paths"  1>> $LOG 2>> $LOG
echo "----------------------------------------"  1>> $LOG 2>> $LOG

echo "" 1>> $LOG 2>> $LOG
echo "LOG $LOG" 1>> $LOG 2>> $LOG
echo "PBS_JOBID $PBS_JOBID" 1>> $LOG 2>> $LOG
echo "TASK $TASK" 1>> $LOG 2>> $LOG
echo "TASKDIR $TASKDIR" 1>> $LOG 2>> $LOG
echo "INPUTDIR $INPUTDIR" 1>> $LOG 2>> $LOG
echo "JOBSDIR $JOBSDIR" 1>> $LOG 2>> $LOG
echo "JOBNAME $JOBNAME" 1>> $LOG 2>> $LOG
echo "JOBDIR $JOBDIR" 1>> $LOG 2>> $LOG
echo "OUTPUTDIR $OUTPUTDIR" 1>> $LOG 2>> $LOG
echo "EXECDIR $EXECDIR" 1>> $LOG 2>> $LOG
echo "STDOUT $STDOUT" 1>> $LOG 2>> $LOG
echo "STDERR $STDERR" 1>> $LOG 2>> $LOG
echo "PBS_STDOUT $PBS_STDOUT" 1>> $LOG 2>> $LOG
echo "PBS_STDERR $PBS_STDERR" 1>> $LOG 2>> $LOG
echo "PBS_NODEFILE" 1>> $LOG 2>> $LOG
cat $PBS_NODEFILE 1>> $LOG 2>> $LOG
echo ""  1>> $LOG 2>> $LOG

# saving PBS environmental variables
echo "----------------------------------------"  1>> $LOG 2>> $LOG
echo "PBS Environmental Variables" 1>> $LOG 2>> $LOG
echo "----------------------------------------" 1>> $LOG 2>> $LOG

echo ""   1>> $LOG 2>> $LOG
# PBS env vars
echo "PBS ENV:"  1>> $LOG 2>> $LOG
env | grep PBS  1>> $LOG 2>> $LOG

# PBS JOBID number
echo "PBS_JOBID number"  1>> $LOG 2>> $LOG
PBS_JOBID_NUM=(${PBS_JOBID//./ })
echo "PBS_JOBID_NUM: $PBS_JOBID_NUM" 1>> $LOG 2>> $LOG

# PBS stdout and stderr file paths
PBS_JOB_STDOUT="$PBS_STDOUT/$TASK.o$PBS_JOBID_NUM"
PBS_JOB_STDERR="$PBS_STDERR/$TASK.e$PBS_JOBID_NUM"

# NODEFILE
echo "PBS_NODEFILE:"  1>> $LOG 2>> $LOG
cat $PBS_NODEFILE  1>> $LOG 2>> $LOG
# GPUFILE
echo "PBS_GPUFILE:"  1>> $LOG 2>> $LOG
cat $PBS_GPUFILE  1>> $LOG 2>> $LOG
# MICFILE
echo "PBS_MICFILE:"  1>> $LOG 2>> $LOG
cat $PBS_MICFILE  1>> $LOG 2>> $LOG
echo ""  1>> $LOG 2>> $LOG

# initial
echo ""  1>> $LOG 2>> $LOG
echo "----------------------------------------" 1>> $LOG 2>> $LOG
echo "Initialization" 1>> $LOG 2>> $LOG
echo "----------------------------------------" 1>> $LOG 2>> $LOG

# load profile
echo "------------" 1>> $LOG 2>> $LOG
echo "RUNNING: source /etc/profile" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
source /etc/profile 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# load hpcc modules
echo "------------" 1>> $LOG 2>> $LOG
echo "RUNNING: source /opt/software/modulefiles/setup_modules.sh" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
source /opt/software/modulefiles/setup_modules.sh 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# load specific library modules
# GNU Compilers
echo "------------" 1>> $LOG 2>> $LOG
echo "RUNNING: module load GNU/4.8.3" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
module load GNU/4.8.3 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# CUDA compilers
echo "------------" 1>> $LOG 2>> $LOG
echo "RUNNING: module load CUDA/7.0" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
module load CUDA/7.0 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG


# set environmental variables and other local variables that are used for 
# many types of scripts
# ===============================================================================
echo "------------" 1>> $LOG 2>> $LOG
echo "Setting environmental variables"  1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG

# scratch directory
export SCRATCHDIR=/mnt/ls15/scratch
# your home scratch
export SCRATCH_HOME=$SCRATCH

# the Dickson lab directory
export DICKSON_LAB=/mnt/research/DicksonLab
# your Dickson lab dir
export MY_DICKSON_LAB=$DICKSON_LAB/$USER
# locally installed programs
export DICKSON_LAB_PROGS=${DICKSON_LAB}/programs
# the installed binaries for Dickson lab
export DICKSON_LAB_BIN=${DICKSON_LAB_PROGS}/bin
# directory with installed libraries
export DICKSON_LAB_LIB=${DICKSON_LAB_PROGS}/lib
# Dickson lab scratch directory
export DICKSON_SCRATCH=$SCRATCHDIR/groups/DicksonLab
# your Dickson lab scratch dir
export MY_DICKSON_SCRATCH=$DICKSON_SCRATCH/$USER

# set environmental variables to programs needed
#----------------------------------------
# charmm executables
export CHARMM_DIR=${DICKSON_LAB_PROGS}/charmm
export CHARMM=${CHARMM_DIR}/exec/gnu/charmm
export MPICHARMM=${CHARMM_DIR}/exec/gnu_M/charmm
export OPENMMCHARMM=${CHARMM_DIR}/exec/gnu/charmm_OpenMM6.3

# charmm data
export CHARMMDATA=${DICKSON_LAB}/toppar

# set the path for mmtsb
export MMTSBDIR=$DICKSON_LAB_PROGS/mmtsb

# OpenMM
export OPENMM_ROOT_DIR=/mnt/research/DicksonLab/programs/OpenMM6.3
export OPENMM_PLUGIN_DIR=$OPENMM_ROOT_DIR/lib/plugins
export OPENMM_PLATFORM=CUDA
unset OPENMM_DEVICE


# Anaconda Python paths
export ANACONDA_DIR=$DICKSON_LAB_PROGS/anaconda2
export ANACONDA_BIN=$ANACONDA_DIR/bin
export PYTHONHOME=$ANACONDA_DIR

# Library paths
export LD_LIBRARY_PATH=${DICKSON_LAB_LIB}:${CUDADIR}/lib:${OPENMM_ROOT_DIR}/lib:${OPENMM_PLUGIN_DIR}:${LD_LIBRARY_PATH}

# PATH
export PATH=$ANACONDA_BIN:${DICKSON_LAB_BIN}:${OPENMM_ROOT_DIR}/bin:${MMTSBDIR}/perl:${DICKSON_LAB_PROGS}/charmm/exec/gnu:${PATH}

# ===============================================================================

# change to the exec dir
echo "------------" 1>> $LOG 2>> $LOG
echo "moving to EXECDIR: $EXECDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
cd $EXECDIR 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# remove current contents of the execdir, useful for if running
# interactive job which writes to same dir, harmless if not
echo "------------" 1>> $LOG 2>> $LOG
echo "Removing existing files if they exist in EXECDIR: $EXECDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
rm -rf $EXECDIR/* 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# copy the input files to the execution directory
echo "------------" 1>> $LOG 2>> $LOG
echo "Copying input files from INPUTDIR: $INPUTDIR to EXECDIR: $EXECDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
cp -rf $INPUTDIR/* $EXECDIR/ 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# copy the actual submission script used
echo "------------" 1>> $LOG 2>> $LOG
echo "Copying submission script ${TASKDIR}/${PBS_JOBNAME} to EXECDIR: $EXECDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
cp "${0}" $EXECDIR/ 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# write file names in $EXECDIR to log
echo "------------" 1>> $LOG 2>> $LOG
echo "listing of EXECDIR: $EXECDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
ls $EXECDIR 1>> $LOG 2>> $LOG
echo "" 1>> $LOG 2>> $LOG

# print out the environmental variables after modifications
echo "------------" 1>> $LOG 2>> $LOG
echo "Environmental variables before execution:"  1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
env 1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG

# ------------------------------
# set the parameters that will be used in this script
# ===============================================================================
echo "------------" 1>> $LOG 2>> $LOG
echo "Setting Script parameters"  1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG

TEST_PARAM="HELLO"
TEST_VAL=8
TEST_FILE="test_input.txt"

echo ""   1>> $LOG 2>> $LOG
# ===============================================================================


# ------------------------------
# The code for this script
# ===============================================================================
echo "------------" 1>> $LOG 2>> $LOG
echo "Running script" 1>> $LOG 2>> $LOG
echo "===============================================================================" 1>> $LOG 2>> $LOG

echo "HELLO beginning the script body"
cat $TEST_FILE 1>> $LOG 2>> $LOG
for ((x=0; x<=${TEST_VAL}; x++)); do
    echo ${TEST_PARAM} > "file_${x}.txt" 2>> $LOG;
done

echo "===============================================================================" 1>> $LOG 2>> $LOG
echo "done with script" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG

# ===============================================================================

# move the output files in EXECDIR back to the job output dir
echo "------------" 1>> $LOG 2>> $LOG
echo "moving EXECDIR $EXECDIR to OUTPUTDIR $OUTPUTDIR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
mv $EXECDIR $OUTPUTDIR  1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG


# move the PBS stdout and stderr files to the jobdir
echo "------------" 1>> $LOG 2>> $LOG
echo "PBS STDOUT is in $PBS_JOB_STDOUT" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
echo ""   1>> $LOG 2>> $LOG

echo "------------" 1>> $LOG 2>> $LOG
echo "PBS STDERR $PBS_JOB_STDERR" 1>> $LOG 2>> $LOG
echo "------------" 1>> $LOG 2>> $LOG
