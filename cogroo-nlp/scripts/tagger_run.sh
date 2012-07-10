#
# Copyright (C) 2012 cogroo <cogroo@cogroo.org>
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#         http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# expand_me & factory em todos

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_AC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_DIC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_AC,POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_AC,POS_DIC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_DIC,POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a PERCEPTRON -d cf -o POS_AC,POS_DIC,POS_DICCUT1

##

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_AC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_DIC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_AC,POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_AC,POS_DIC

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_DIC,POS_DICCUT1

perl scripts/eval.pl -t pos -p 8 -e -v gp -a MAXENT -d cf -o POS_AC,POS_DIC,POS_DICCUT1


