import './Dashboard.css';
import {useEffect, useState} from 'react';
import Api from '../axios/config';
import Table from '../components/Table';
import {useNavigate} from 'react-router-dom'
import { toast } from 'react-toastify';


function Dashboard() {

  const[servers, setServers] = useState([]);
  const[filteredServer, setFilteredServer] = useState([])
  const[server, setServer] = useState({});

  let navigate = useNavigate();

  function printReport(){
    let dataType = 'application/vnd.ms-excel.sheet.macroEnabled.12';
    let tableSelect = document.getElementById('servers');
    let tableHtml = tableSelect.outerHTML.replace(/ /g, '%20');
    let downloadLink = document.createElement('a');
    document.body.appendChild(downloadLink);
    downloadLink.href = 'data:' + dataType + ', ' + tableHtml;
    downloadLink.download = 'server-report.xls';
    downloadLink.click();
    document.body.removeChild(downloadLink);
  }

  function filterServer(value){

    if(value === "SERVER_UP"){
      return setFilteredServer(servers.filter((server) => server.status === "SERVER_UP"))
    }else if(value === "SERVER_DOWN"){
      return setFilteredServer(servers.filter((server) => server.status === "SERVER_DOWN"))
    }else{
      return setFilteredServer(servers)
    }
  }
  useEffect(() =>{
    const fetchServer = async () => {
      await Api.get('/list', {withCredentials: true})
      .then((resp) =>{
        setServers(resp.data.content)
        setFilteredServer(resp.data.content)
        console.log(resp.data.content)
      }).catch(err => console.log(err))
    }
    
    fetchServer()

  }, [])

  function filterOnChange(e){
    filterServer(e.target.value)
  }
  function handleOnChange(e){
    setServer({...server, [e.target.name]: e.target.value})
  }

  async function createServer(server){
      await Api.post("/save", server, {withCredentials: true})
      .then((resp) => {
        navigate("/")
        toast.success("Servidor criado com sucesso")
    })
      .catch(err => {
        console.log(err)
        //toast.error(err.getMessage)
      }
      )
  }

  function submit(e){
      e.preventDefault()
      console.log(server)
      createServer(server)
  }


  return (
    <div>
      <div className="container-xl">
        <div className="table-responsive">
         <div className="table-wrapper">
          <div className="table-title">
              <div className="row">
                  <div className="col-sm-6">
                      <h2>Manage Servers</h2>
                  </div>
                  <div className="col-sm-6">
                      <button  type="button" className="btn btn-primary" onClick={printReport}>Print Report</button>

                      <a href="#addEmployeeModal" data-target="#addEmployeeModal" className="btn btn-success" data-bs-toggle="modal">
                          <span>New Server</span>
                      </a>

                      <span>
                          <select name="status" id='status'  
                              className="btn btn-info" onChange={filterOnChange} >
                              <option value="ALL">ALL</option>
                              <option value="SERVER_UP">SERVER UP</option>
                              <option value="SERVER_DOWN">SERVER DOWN</option>
                          </select>
                      </span>
                  </div>
              </div>
          </div> <br/>
            <Table servers={filteredServer} setServers={setServers}/>       
        </div>
      </div>
    </div>
    <div id="addEmployeeModal" className="modal fade">
      <div className="modal-dialog">
        <div className="modal-content">
            <form  onSubmit={submit}>
                <div className="modal-header">
                    <h4 className="modal-title">Add Server</h4>
                    <button type="button" className="close" data-bs-dismiss="modal" aria-hidden="true">&times;</button>
                </div>
                <div className="modal-body">
                    <div className="form-group">
                        <label>IP</label>
                        <input type="text" name="ipAddress" className="form-control" required onChange={handleOnChange}/>
                    </div>
                    <div className="form-group">
                        <label>Name</label>
                        <input type="text" name="name" className="form-control" required onChange={handleOnChange}/>
                    </div>
                    <div className="row">
                        <div className="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-6">
                            <div className="form-group">
                                <label>Memory</label>
                                <input type="text" name="memory" className="form-control" required onChange={handleOnChange}/>
                            </div>
                        </div>
                        <div className="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-6">
                            <div className="form-group">
                                <label>Type</label>
                                <input type="text" name="type" className="form-control" required onChange={handleOnChange}/>
                            </div>
                        </div>
                    </div>
                    <div className="form-group">
                        <label>Status</label>
                        <select name="status" className="form-control" required onChange={handleOnChange}>
                            <option value="SERVER_UP">SERVER UP</option>
                            <option value="SERVER_DOWN">SERVER DOWN</option>
                        </select>
                    </div>
                </div>
                <div className="modal-footer">
                    <button type="button" className="btn btn-warning" id="closeModal" data-bs-dismiss="modal">
                        Cancel
                    </button>
                    <button type="submit" 
                        className="btn btn-success" data-bs-dismiss="modal">
                        <span >Add</span>
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>
</div>
  );
}

export default Dashboard;
