const detailRoot = document.getElementById("exam-detail-root");
const path = window.location.pathname;
const parts = path.split('/');
const examId = parts[parts.length - 1];

render(examId);

function render(examId){
    ReactDOM.render(<SubjectList examId={examId}/>,detailRoot);
}


function SubjectList({ examId }) {
    const [subjects, setSubjects] = React.useState([]);
    const [examData, setExamData] = React.useState(null);
    const [data, setData] = React.useState(null);
    const [ childData, setChildData ] = React.useState([]);

    const fetchData = async (retryCount = 0) => {
        try {
            const response = await fetch(`/api/score/${examId}/1`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const jsonData = await response.json();
            setData(jsonData.content);
        } catch (error) {
            console.error('Error fetching data:', error);
            if (retryCount < 3) {
                setTimeout(() => fetchData(retryCount + 1), 300);
            } else {
                // location.href = "/"                
            }
        }
    };
    const fetchSubjectData = async (retryCount = 0) => {
        try {
            const response = await fetch("/api/subject/");
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const subjectData = await response.json();
            setSubjects(subjectData);
        } catch (error) {
            console.error('Error fetching data:', error);
            if (retryCount < 3) {
                setTimeout(() => fetchSubjectData(retryCount + 1), 300);
            } else {
                // location.href = "/"                
            }
        }
    };
    const fetchExamData = async (retryCount = 0) => {
        try {
            const response = await fetch(`/api/exam/${examId}`);
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const examTempData = await response.json();
            setExamData(examTempData);
        } catch (error) {
            console.error('Error fetching data:', error);
            if (retryCount < 3) {
                setTimeout(() => fetchExamData(retryCount + 1), 500);
            } else {
                // location.href = "/"                
            }
        }
    };
    const fetchSubmit = async (examId, data) => {
        console.log("submit data",data)
        
        const response = await fetch(`/api/score/${examId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
              },
            credentials: "include",
            body: JSON.stringify(data)
        });
        if (response.ok) {
            console.log("성공",response);
        } else {
            console.log("실패",response);
        }
        return response;
    };
    const fetchModify = async (examId, scoreId, data) => {
        const response = await fetch(`/api/score/${examId}/${scoreId}`, {
            method: "PUT",
            credentials: "include",
            body: data
        });
        if (response.ok) {
            console.log("성공");
        }
    };
    React.useEffect(() => {
        fetchExamData();
        fetchSubjectData();
        fetchData();
    }, []);
    const handleDataChange = (data) => {
        setChildData(prevChildData => {
            const index = prevChildData.findIndex(item => item.id.subjectCode === data.id.subjectCode);
            if (index === -1) {
                return [...prevChildData, data];
            } else {
                const newChildData = [...prevChildData];
                newChildData[index] = data;
                return newChildData;
            }
        });
    }

    const handleSubmitAll = () => {
        console.log(childData);
        const submitData = [];
        childData.forEach(data => {
            submitData.push(data.scoreData)
        });
        fetchSubmit(examId,submitData);
    }


    return (
        <>
            <div className="container">
                <div className="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <button
                            className="btn btn-primary"
                            onClick={() => { location.href = "/exam/list/1" }}
                        >
                            시험목록
                        </button>
                    </div>
                    <div className="text-center exam-detail__title">
                        <span className="d-block">{examData?.examType.examTypeName}</span>
                        <span className="d-block">{examData?.examName}</span>
                        <span className="d-block">{examData?.examDate}</span>
                    </div>
                    <div>
                        <button className="btn btn-success" onClick={handleSubmitAll}>등록</button>
                    </div>
                </div>
            </div>
            <table className="table table-striped mt-4">
                <thead>
                    <tr>
                        <th>과목</th>
                        <th>점수</th>
                        <th>표준점수</th>
                        <th>등급</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    {data && subjects?.map((subject, index) => 
                        <Subject
                            key={subject.subjectCode}
                            examId={examId}
                            subject={subject}
                            data={data}
                            onDataChange={handleDataChange}
                        />
                    )}
                </tbody>
            </table>
        </>
    );
}

const Subject = ({examId, subject, data, onDataChange }) => {
    const [scoreId, setScoreId] = React.useState(null);
    const [score, setScore] = React.useState(0);
    const [exScore, setExScore] = React.useState(100);
    const [std, setStd] = React.useState(0);
    const [grade, setGrade] = React.useState(1);
    const [isModify, setIsModify] = React.useState(false);

    React.useEffect(() => {
        const item = data.find(item => item.subject.subjectCode === subject.subjectCode);
        if (item) {
            setScoreId(item.scoreId || null);
            setScore(item.scoreScore || 0);
            setExScore(item.scoreExScore || 100);
            setStd(item.scoreStdScore || 0);
            setGrade(item.scoreGrade || 1);
        }
    }, [isModify]);

    React.useEffect(() => {
        scoreId ? setIsModify(true) : setIsModify(false);
    }, [scoreId]);

    React.useEffect(() => {
        onDataChange({
            id:subject,
            scoreData:{
                examId:examId,
                subjectCode:subject.subjectCode,
                scoreScore:score,
                scoreExScore:exScore,
                scoreStdScore:std,
                scoreGrade:grade
            }
        })
    },[scoreId,score,exScore,std,grade]);

    return (
        <tr>
          <td>{subject.subject}</td>
            <td>
                <input className="exam-detail__row-input ed-score" value={score} required onChange={(event) => setScore(event.target.value)} type="text" name="scoreScore" />
                <input className="exam-detail__row-input ed-exscore" value={exScore} required onChange={(event) => setExScore(event.target.value)} type="text" name="scoreExScore" />
            </td>
            <td>
                <input className="exam-detail__row-input ed-std" value={std} required onChange={(event) => setStd(event.target.value)} type="text" name="scoreStdScore" />
            </td>
            <td>
                <input className="exam-detail__row-input ed-grade" value={grade} required onChange={(event) => setGrade(event.target.value)} type="text" name="scoreGrade" />
            </td>
            <td>
            </td>
        </tr>
    );
};
